package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.transactionhistoryservice.service.TransactionHistoryService;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction-history")
public class TransactionHistoryController {
    
    private final TransactionHistoryService transactionHistoryService;

    @GetMapping
    public List<TransactionHistoryDTO> list() {
        //SecurityUtil
        String userId = "seller@directdeal.co.kr";
        return transactionHistoryService.list(userId);
    }

    @PutMapping("/setbuyer")
    public void setBuyer(@RequestBody TransactionHistoryDTO dto) {
        // get userId from SecurityUtil
        // 동일 아이디 여부 확인할 것
        log.debug("TransactionHistoryController.setBuyer(), dto => " + dto);
        transactionHistoryService.setBuyer(dto);
    }
}
