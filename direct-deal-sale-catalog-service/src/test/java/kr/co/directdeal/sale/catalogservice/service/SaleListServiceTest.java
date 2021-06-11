package kr.co.directdeal.sale.catalogservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.service.mapper.SaleListMapper;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListRepository;

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

        given(saleListRepository.findAll(any(Pageable.class)))
            .willReturn(new PageImpl<SaleList>(Collections.singletonList(saleList), PageRequest.of(0,1), 1));
        
        //when
        List<SaleListDTO> resultList = saleListService.list(PageRequest.of(0,1));
        
        //then
        assertThat(resultList.isEmpty(), equalTo(false));
        assertThat(resultList.get(0).getId(), equalTo(id));
    }

    @Test
    public void List_ValidPageable_ReturnEmptyList() {
        //given
        given(saleListRepository.findAll(any(Pageable.class)))
            .willReturn(new PageImpl<SaleList>(Collections.emptyList(), PageRequest.of(0,1), 0));
        
        //when
        List<SaleListDTO> resultList = saleListService.list(PageRequest.of(0,1));
        
        //then
        assertThat(resultList.isEmpty(), equalTo(true));
    }

    @Test
    public void LiftUp_InvalidId_ThrowSaleListException() {
        //given
        String id = "1";
        given(saleListRepository.findById(id))
            .willReturn(Optional.empty());                   
                                
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
            .willReturn(Optional.of(saleList));           

        given(saleListDomainService.canLiftUp(saleList))
            .willReturn(true);                   
                                
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
            .willReturn(Optional.of(saleList));           

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
