package kr.co.directdeal.sale.catalogservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.service.mapper.SaleItemMapper;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleItemRepository;

@ExtendWith(MockitoExtension.class)
public class SaleItemServiceTest {

    @Mock
    private SaleItemRepository saleItemRepository;

    private SaleItemService saleItemService;

    private Mapper<SaleItem, SaleItemDTO> saleItemMapper = new SaleItemMapper();

    @BeforeEach
    public void init() {
        this.saleItemService = new SaleItemService(
                                        saleItemRepository, 
                                        saleItemMapper);
    }
    
    @Test
    public void FindSaleItemById_InvalidId_ThrowSaleItemException() {
        //given
        String id = "1";
        given(saleItemRepository.findById(id))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(SaleItemException.class, () -> {
            saleItemService.findSaleItemById(id);
        });
    }

    @Test
    public void FindSaleItemById_ValidId_Success() {
        //given
        String id = "1";
        SaleItem saleItem = SaleItem.builder()
                                .id(id)
                                .build();                       
                                
        given(saleItemRepository.findById(id))
            .willReturn(Optional.of(saleItem));

        //when
        SaleItemDTO saleItemDTO = saleItemService.findSaleItemById(id);

        //then
        assertThat(saleItemDTO.getId(), equalTo(id));
        verify(saleItemRepository).findById(id);
    }
}
