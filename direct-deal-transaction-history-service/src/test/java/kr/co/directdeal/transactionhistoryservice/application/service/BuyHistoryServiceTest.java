package kr.co.directdeal.transactionhistoryservice.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.object.BuyHistory;
import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.application.service.mapper.BuyHistoryMapper;
import kr.co.directdeal.transactionhistoryservice.port.outbound.BuyHistoryRepository;

@ExtendWith(MockitoExtension.class) 
public class BuyHistoryServiceTest {

    @Mock
    private BuyHistoryRepository buyHistoryRepository;

    private BuyHistoryService buyHistoryService;

    @Spy
    private Mapper<BuyHistory, BuyHistoryDTO> mapper = new BuyHistoryMapper();

    @BeforeEach
    public void init() {
        this.buyHistoryService = new BuyHistoryService(
                                                buyHistoryRepository, 
                                                mapper);
    }

    @Test
    public void Insert_ValidBuyHistoryDTO_SUCCESS() {
        //given
        BuyHistoryDTO buyHistoryDTO = 
            BuyHistoryDTO.builder()
                // .id(1L)
                .itemId("9aaf0862-84b8-4fe9-b1bd-65261eaa334b")
                .buyerId("buyer@directdeal.co.kr")
                .title("M1 Macbook Air")
                .category("Notebook")
                .targetPrice(1000000)
                .sellerId("seller@directdeal.co.kr")
                .completionTime(Instant.now())
                .build();

        //when
        buyHistoryService.insert(buyHistoryDTO);

        //then
        verify(mapper).toEntity(buyHistoryDTO);
        verify(buyHistoryRepository).save(mapper.toEntity(buyHistoryDTO));
    }

    @Test
    public void Delete_InvalidBuyId_ThrowbuyHistoryException() {
        //given
        BuyHistoryDTO dto = 
            BuyHistoryDTO.builder()
                .id(1L)
                .buyerId("buyer@directdeal.co.kr")
                .build();

        given(buyHistoryRepository.findByIdAndBuyerId(dto.getId(), dto.getBuyerId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(BuyHistoryException.class, () -> {
            buyHistoryService.delete(dto);
        });
    }
    
}
