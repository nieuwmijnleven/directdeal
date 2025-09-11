package kr.co.directdeal.sale.catalogservice.webflux.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import kr.co.directdeal.sale.catalogservice.webflux.domain.service.SaleListDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.mapper.SaleListMapper;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleListRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class SaleListServiceTest {

    @Mock
    private SaleListRepository saleListRepository;

    private SaleListService saleListService;

    private Mapper<SaleList, SaleListDTO> saleListMapper = new SaleListMapper();

    @Mock
    private SaleListDomainService saleListDomainService;

    @BeforeEach
    public void init() {
        this.saleListService = new SaleListService(
                                        saleListRepository, 
                                        saleListMapper,
                                        saleListDomainService);
    }
    
    @Test
    public void List_ValidPageable_ReturnList() {
        //given
        String id = "1";
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .build();    

        given(saleListRepository.findAllByStatus(any(Pageable.class), any(SaleItemStatus.class)))
            .willReturn(Flux.just(saleList));
        
        //when
        Flux<SaleListDTO> resultList = saleListService.list(PageRequest.of(0,1));

        //then
        assertThat(resultList.count().block(), equalTo(1L));
        assertThat(resultList.blockFirst().getId(), equalTo(id));
    }

    @Test
    public void List_ValidPageable_ReturnEmptyList() {
        //given
        given(saleListRepository.findAllByStatus(any(Pageable.class), any(SaleItemStatus.class)))
            .willReturn(Flux.empty());
        
        //when
        Flux<SaleListDTO> resultList = saleListService.list(PageRequest.of(0,1));
        
        //then
        assertThat(resultList.count().block(), equalTo(0L));
    }

    @Test
    public void LiftUp_InvalidId_ThrowSaleListException() {
        //given
        String id = "1";
        given(saleListRepository.findById(id))
            .willReturn(Mono.empty());                   
                                
        //when and then
        assertThrows(SaleListException.class, () -> {
            saleListService.liftUp(id);
        });
    }

    @Test
    public void LiftUp_ValidIdAndCanLiftUpIsTrue_ReturnTrue() {
        //given
        String id = "1";
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .build();

        given(saleListRepository.findById(id))
            .willReturn(Mono.just(saleList));           

        given(saleListDomainService.canLiftUp(saleList))
            .willReturn(true);      
            
        given(saleListRepository.save(any(SaleList.class)))
            .willReturn(Mono.just(saleList));
                                
        //when
        boolean result = saleListService.liftUp(id);

        //then
        verify(saleListRepository).findById(id);
        verify(saleListDomainService).canLiftUp(saleList);
        
        assertThat(result, equalTo(true));
    }

    @Test
    public void LiftUp_ValidIdAndCanLiftUpIsFalse_ReturnFalse() {
        //given
        String id = "1";
        SaleList saleList = SaleList.builder()
                                        .id(id)
                                        .build();

        given(saleListRepository.findById(id))
            .willReturn(Mono.just(saleList));           

        given(saleListDomainService.canLiftUp(saleList))
            .willReturn(false);                   
                                
        //when
        boolean result = saleListService.liftUp(id);

        //then
        verify(saleListRepository).findById(id);
        verify(saleListDomainService).canLiftUp(saleList);
        
        assertThat(result, equalTo(false));
    }
}
