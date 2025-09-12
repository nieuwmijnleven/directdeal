package kr.co.directdeal.transactionhistoryservice.port.inbound;

import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;

import java.util.List;

/**
 * Use case interface for Buy History operations.
 * Provides methods to list, insert, and delete buy history records.
 *
 * @author Cheol Jeon
 */
public interface BuyHistoryUseCase {

    /**
     * Retrieves the list of BuyHistoryDTO for the given userId.
     *
     * @param userId the ID of the user
     * @return list of BuyHistoryDTO
     */
    List<BuyHistoryDTO> list(String userId);

    /**
     * Inserts a new buy history record.
     *
     * @param dto the BuyHistoryDTO to insert
     */
    void insert(BuyHistoryDTO dto);

    /**
     * Deletes an existing buy history record.
     *
     * @param dto the BuyHistoryDTO to delete
     */
    void delete(BuyHistoryDTO dto);
}
