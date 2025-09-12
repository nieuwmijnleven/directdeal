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

/**
 * Service class for handling transaction history operations.
 * Implements {@link TransactionHistoryUseCase}.
 *
 * Provides functionality to list, insert, set buyer, and delete transaction histories.
 * Publishes {@link BuyerSetEvent} when a buyer is assigned.
 *
 */
@Service
@RequiredArgsConstructor
public class TransactionHistoryService implements TransactionHistoryUseCase {

    private final TransactionHistoryRepository transactionHistoryRepository;

    private final Mapper<TransactionHistory, TransactionHistoryDTO> mapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Retrieves transaction history list for the seller.
     *
     * @param userId seller's user ID
     * @return list of {@link TransactionHistoryDTO}
     */
    @Transactional(readOnly = true)
    @Override
    public List<TransactionHistoryDTO> list(String userId) {
        return transactionHistoryRepository.findAllBySellerId(userId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new transaction history entry.
     *
     * @param dto the transaction history DTO to insert
     */
    @Transactional
    @Override
    public void insert(TransactionHistoryDTO dto) {
        TransactionHistory newTransactionHistory = mapper.toEntity(dto);
        transactionHistoryRepository.save(newTransactionHistory);
    }

    /**
     * Sets the buyer for a transaction history.
     * Only the seller can assign the buyer.
     * Publishes a {@link BuyerSetEvent} after setting.
     *
     * @param dto transaction history DTO containing buyer info and transaction ID
     * @throws TransactionHistoryException if transaction not found or seller mismatch
     */
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
                    .messageArgs(new String[]{userId, transactionHistory.getSellerId()})  // 수정: dto.getSellerId() → transactionHistory.getSellerId()
                    .build();

        transactionHistory.setBuyerId(dto.getBuyerId());
        applicationEventPublisher.publishEvent(new BuyerSetEvent(mapper.toDTO(transactionHistory)));
    }

    /**
     * Deletes a transaction history entry by ID.
     *
     * @param dto transaction history DTO containing ID to delete
     * @throws TransactionHistoryException if transaction not found
     */
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
