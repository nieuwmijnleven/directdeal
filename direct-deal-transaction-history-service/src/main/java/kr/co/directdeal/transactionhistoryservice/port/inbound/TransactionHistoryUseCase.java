package kr.co.directdeal.transactionhistoryservice.port.inbound;

import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;

import java.util.List;

/**
 * Use case interface for Transaction History operations.
 * Provides methods to list, insert, set buyer, and delete transaction history records.
 *
 * @author Cheol Jeon
 */
public interface TransactionHistoryUseCase {

    /**
     * Retrieves the list of TransactionHistoryDTO for the given userId.
     *
     * @param userId the ID of the user
     * @return list of TransactionHistoryDTO
     */
    List<TransactionHistoryDTO> list(String userId);

    /**
     * Inserts a new transaction history record.
     *
     * @param dto the TransactionHistoryDTO to insert
     */
    void insert(TransactionHistoryDTO dto);

    /**
     * Sets the buyer for an existing transaction history record.
     *
     * @param dto the TransactionHistoryDTO containing the buyer information to set
     */
    void setBuyer(TransactionHistoryDTO dto);

    /**
     * Deletes an existing transaction history record.
     *
     * @param dto the TransactionHistoryDTO to delete
     */
    void delete(TransactionHistoryDTO dto);
}
