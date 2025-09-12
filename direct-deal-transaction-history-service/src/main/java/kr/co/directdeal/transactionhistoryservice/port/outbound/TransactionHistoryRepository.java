package kr.co.directdeal.transactionhistoryservice.port.outbound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.transactionhistoryservice.domain.object.TransactionHistory;

/**
 * Repository interface for TransactionHistory entity.
 * Provides CRUD operations and custom query methods for TransactionHistory.
 *
 * @author Cheol Jeon
 */
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    /**
     * Finds all TransactionHistory entities for a given seller ID.
     *
     * @param sellerId the ID of the seller
     * @return list of TransactionHistory entities
     */
    List<TransactionHistory> findAllBySellerId(String sellerId);

    /**
     * Finds all TransactionHistory entities for a given buyer ID.
     *
     * @param buyerId the ID of the buyer
     * @return list of TransactionHistory entities
     */
    List<TransactionHistory> findAllByBuyerId(String buyerId);
}
