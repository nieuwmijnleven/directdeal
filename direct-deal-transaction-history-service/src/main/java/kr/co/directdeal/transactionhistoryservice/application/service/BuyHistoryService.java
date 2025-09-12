package kr.co.directdeal.transactionhistoryservice.application.service;

import java.util.List;
import java.util.stream.Collectors;

import kr.co.directdeal.transactionhistoryservice.port.inbound.BuyHistoryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.object.BuyHistory;
import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.port.outbound.BuyHistoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service class for managing buy history operations.
 * Implements {@link BuyHistoryUseCase} to provide business logic.
 *
 * Handles listing, inserting, and deleting buy history entries.
 *
 * @author Cheol
 */
@Service
@RequiredArgsConstructor
public class BuyHistoryService implements BuyHistoryUseCase {

    private final BuyHistoryRepository buyHistoryRepository;

    private final Mapper<BuyHistory, BuyHistoryDTO> mapper;

    /**
     * Retrieves the list of buy history records for a given user.
     *
     * @param userId the ID of the buyer
     * @return list of {@link BuyHistoryDTO} corresponding to the buyer's history
     */
    @Transactional(readOnly = true)
    @Override
    public List<BuyHistoryDTO> list(String userId) {
        return buyHistoryRepository.findAllByBuyerId(userId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new buy history record.
     * Checks for duplicate transaction by buyerId, itemId, and completionTime before saving.
     * Throws {@link BuyHistoryException} if a duplicate is detected.
     *
     * @param dto the {@link BuyHistoryDTO} to insert
     */
    @Transactional
    @Override
    public void insert(BuyHistoryDTO dto) {
        boolean checkDuplicate =
                buyHistoryRepository
                        .existsByBuyerIdAndItemIdAndCompletionTime(dto.getBuyerId(), dto.getItemId(), dto.getCompletionTime());
        if (checkDuplicate) {
            throw BuyHistoryException.builder()
                    .messageKey("buyhistoryservice.exception.insert.transaction.duplicate.message")
                    .messageArgs(new String[]{})
                    .build();
        }

        BuyHistory newBuyHistory = mapper.toEntity(dto);
        buyHistoryRepository.save(newBuyHistory);
    }

    /**
     * Deletes a buy history record by its ID and buyerId.
     * Throws {@link BuyHistoryException} if the record is not found.
     *
     * @param dto the {@link BuyHistoryDTO} containing the ID and buyerId to delete
     */
    @Transactional
    @Override
    public void delete(BuyHistoryDTO dto) {
        BuyHistory buyHistory =
                buyHistoryRepository.findByIdAndBuyerId(dto.getId(), dto.getBuyerId())
                        .orElseThrow(() -> BuyHistoryException.builder()
                                .messageKey("buyhistoryservice.exception.delete.transaction.notfound.message")
                                .messageArgs(new String[]{ dto.getId().toString(), dto.getBuyerId() })
                                .build());

        buyHistoryRepository.delete(buyHistory);
    }
}
