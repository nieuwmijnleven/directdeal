package kr.co.directdeal.transactionhistoryservice.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.co.directdeal.transactionhistoryservice.port.inbound.TransactionHistoryUseCase;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.domain.object.TransactionHistory;
import kr.co.directdeal.transactionhistoryservice.event.BuyerSetEvent;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.port.outbound.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService implements TransactionHistoryUseCase {
       
    private final TransactionHistoryRepository transactionHistoryRepository;

    private final Mapper<TransactionHistory, TransactionHistoryDTO> mapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    @Override
    public List<TransactionHistoryDTO> list(String userId) {
        return transactionHistoryRepository.findAllBySellerId(userId).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void insert(TransactionHistoryDTO dto) {
        TransactionHistory newTransactionHistory = mapper.toEntity(dto);
        transactionHistoryRepository.save(newTransactionHistory);
    }

    @Transactional
    @Override
    public void setBuyer(TransactionHistoryDTO dto) {
        TransactionHistory transactionHistory = 
            transactionHistoryRepository.findById(dto.getId())
                .orElseThrow(() -> TransactionHistoryException.builder()
                                        .messageKey("transactionhistoryservice.exception.setbuyer.transaction.notfound.message")
                                        .messageArgs(new String[]{ dto.getId().toString() })
                                        .build());

        String userId = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(userId, transactionHistory.getSellerId()))
            throw TransactionHistoryException.builder()
                    .messageKey("transactionhistorycontroller.exception.setbuyer.notthesame.message")
                    .messageArgs(new String[]{userId, dto.getSellerId()})
                    .build();

        transactionHistory.setBuyerId(dto.getBuyerId());
        applicationEventPublisher.publishEvent(new BuyerSetEvent(mapper.toDTO(transactionHistory)));
    }

    //@PreAuthority(admin)
    @Transactional
    @Override
    public void delete(TransactionHistoryDTO dto) {
        TransactionHistory transactionHistory = 
            transactionHistoryRepository.findById(dto.getId())
                .orElseThrow(() -> TransactionHistoryException.builder()
                                        .messageKey("transactionhistoryservice.exception.delete.transaction.notfound.message")
                                        .messageArgs(new String[]{ dto.getId().toString() })
                                        .build());

        transactionHistoryRepository.delete(transactionHistory);
    }
}
