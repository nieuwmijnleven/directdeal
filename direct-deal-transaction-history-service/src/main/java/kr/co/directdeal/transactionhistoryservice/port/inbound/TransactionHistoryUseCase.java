package kr.co.directdeal.transactionhistoryservice.port.inbound;

import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionHistoryUseCase {
    List<TransactionHistoryDTO> list(String userId);

    void insert(TransactionHistoryDTO dto);

    void setBuyer(TransactionHistoryDTO dto);

    void delete(TransactionHistoryDTO dto);
}
