package kr.co.directdeal.transactionhistoryservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.TransactionHistory;
import kr.co.directdeal.transactionhistoryservice.event.BuyerSetEvent;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
       
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final Mapper<TransactionHistory, TransactionHistoryDTO> mapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public List<TransactionHistoryDTO> list(String userId) {
        return transactionHistoryRepository.findAllBySellerId(userId).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    public void insert(TransactionHistoryDTO dto) {
        TransactionHistory newTransactionHistory = mapper.toEntity(dto);
        transactionHistoryRepository.save(newTransactionHistory);
    }

    @Transactional
    public void setBuyer(TransactionHistoryDTO dto) {
        TransactionHistory transactionHistory = 
            transactionHistoryRepository.findById(dto.getId())
                .orElseThrow(() -> TransactionHistoryException.builder()
                                        .messageKey("transactionhistoryservice.exception.setbuyer.transaction.notfound.message")
                                        .messageArgs(new String[]{ dto.getId() })
                                        .build());

        transactionHistory.setBuyerId(dto.getBuyerId());
        applicationEventPublisher.publishEvent(new BuyerSetEvent(mapper.toDTO(transactionHistory)));
    }

    //@PreAuthority(admin)
    @Transactional
    public void delete(TransactionHistoryDTO dto) {
        TransactionHistory transactionHistory = 
            transactionHistoryRepository.findById(dto.getId())
                .orElseThrow(() -> TransactionHistoryException.builder()
                                        .messageKey("transactionhistoryservice.exception.delete.transaction.notfound.message")
                                        .messageArgs(new String[]{ dto.getId() })
                                        .build());

        transactionHistoryRepository.delete(transactionHistory);
    }
}
