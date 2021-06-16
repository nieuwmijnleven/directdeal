package kr.co.directdeal.transactionhistoryservice.service;

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
import org.springframework.context.ApplicationEventPublisher;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.TransactionHistory;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.mapper.TransactionHistoryMapper;
import kr.co.directdeal.transactionhistoryservice.service.repository.TransactionHistoryRepository;

@ExtendWith(MockitoExtension.class) 
public class TransactionHistoryServiceTest {

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    private TransactionHistoryService transactionHistoryService;

    @Spy
    private Mapper<TransactionHistory, TransactionHistoryDTO> mapper = new TransactionHistoryMapper();

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    public void init() {
        this.transactionHistoryService = new TransactionHistoryService(
                                                transactionHistoryRepository, 
                                                mapper,
                                                applicationEventPublisher);
    }

    @Test
    public void Insert_ValidTransactionHistoryDTO_SUCCESS() {
        //given
        TransactionHistoryDTO transactionHistoryDTO = 
            TransactionHistoryDTO.builder()
                // .id(1L)
                .itemId("9aaf0862-84b8-4fe9-b1bd-65261eaa334b")
                .sellerId("seller@directdeal.co.kr")
                .title("M1 Macbook Air")
                .category("Notebook")
                .targetPrice(1000000)
                .buyerId("buyer@directdeal.co.kr")
                .completionTime(Instant.now())
                .build();

        //when
        transactionHistoryService.insert(transactionHistoryDTO);

        //then
        verify(mapper).toEntity(transactionHistoryDTO);
        verify(transactionHistoryRepository).save(mapper.toEntity(transactionHistoryDTO));
    }

    @Test
    public void SetBuyer_InvalidTransactionId_ThrowTransactionHistoryException() {
        //given
        TransactionHistoryDTO transactionHistoryDTO = 
            TransactionHistoryDTO.builder()
                .id(1L)
                .buyerId("buyer@directdeal.co.kr")
                .build();

        given(transactionHistoryRepository.findById(transactionHistoryDTO.getId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(TransactionHistoryException.class, () -> {
            transactionHistoryService.setBuyer(transactionHistoryDTO);
        });
    }

    @Test
    public void Delete_InvalidTransactionId_ThrowTransactionHistoryException() {
        //given
        TransactionHistoryDTO transactionHistoryDTO = 
            TransactionHistoryDTO.builder()
                .id(1L)
                .buyerId("buyer@directdeal.co.kr")
                .build();

        given(transactionHistoryRepository.findById(transactionHistoryDTO.getId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(TransactionHistoryException.class, () -> {
            transactionHistoryService.delete(transactionHistoryDTO);
        });
    }
    
}
