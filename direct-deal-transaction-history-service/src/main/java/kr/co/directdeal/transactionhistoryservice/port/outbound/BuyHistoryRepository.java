package kr.co.directdeal.transactionhistoryservice.port.outbound;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.transactionhistoryservice.domain.object.BuyHistory;

/**
 * Repository interface for BuyHistory entity.
 * Provides CRUD operations and custom query methods for BuyHistory.
 *
 * @author Cheol Jeon
 */
@Repository
public interface BuyHistoryRepository extends JpaRepository<BuyHistory, Long> {

    /**
     * Finds a BuyHistory entity by its ID and buyer ID.
     *
     * @param id the ID of the BuyHistory
     * @param buyerId the ID of the buyer
     * @return an Optional containing the found BuyHistory or empty if not found
     */
    Optional<BuyHistory> findByIdAndBuyerId(Long id, String buyerId);

    /**
     * Finds all BuyHistory entities for a given buyer ID.
     *
     * @param buyerId the ID of the buyer
     * @return list of BuyHistory entities
     */
    List<BuyHistory> findAllByBuyerId(String buyerId);

    /**
     * Checks if a BuyHistory exists by buyer ID, item ID, and completion time.
     *
     * @param buyerId the ID of the buyer
     * @param itemId the ID of the item
     * @param completionTime the completion time
     * @return true if such a BuyHistory exists, false otherwise
     */
    boolean existsByBuyerIdAndItemIdAndCompletionTime(String buyerId, String itemId, Instant completionTime);
}
