package kr.co.directdeal.transactionhistoryservice.service.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.transactionhistoryservice.domain.BuyHistory;

@Repository
public interface BuyHistoryRepository extends JpaRepository<BuyHistory, String> {
    Optional<BuyHistory> findByIdAndBuyerId(String id, String buyerId);
    List<BuyHistory> findAllByBuyerId(String buyerId);
    boolean existsByBuyerIdAndItemIdAndCompletionTime(String buyerId, String itemId, Instant completionTime);
}
