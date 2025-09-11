package kr.co.directdeal.transactionhistoryservice.port.inbound;

import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BuyHistoryUseCase {
    List<BuyHistoryDTO> list(String userId);

    void insert(BuyHistoryDTO dto);

    void delete(BuyHistoryDTO dto);
}
