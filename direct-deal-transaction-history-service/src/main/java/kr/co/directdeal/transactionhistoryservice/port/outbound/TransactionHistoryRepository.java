package kr.co.directdeal.transactionhistoryservice.port.outbound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.transactionhistoryservice.domain.object.TransactionHistory;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findAllBySellerId(String sellerId);
    List<TransactionHistory> findAllByBuyerId(String buyerId);
}
