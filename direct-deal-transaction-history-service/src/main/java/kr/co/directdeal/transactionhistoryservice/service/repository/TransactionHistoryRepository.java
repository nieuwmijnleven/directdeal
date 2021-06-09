package kr.co.directdeal.transactionhistoryservice.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.transactionhistoryservice.domain.TransactionHistory;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
    List<TransactionHistory> findAllBySellerId(String sellerId);
    List<TransactionHistory> findAllByBuyerId(String buyerId);
}
