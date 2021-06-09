package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.transactionhistoryservice.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/buy-history")
public class BuyHistoryController {
    
    private final BuyHistoryService buyHistoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BuyHistoryDTO> list() {
        //SecurityUtil
        String userId = "seller@directdeal.co.kr";
        return buyHistoryService.list(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        // get userId from SecurityUtil
        // 동일 아이디 여부 확인할 것
        //String buyerId = SecurityUtil.getUserId();
        String buyerId = "buyer@directdeal.co.kr";
        buyHistoryService.delete(BuyHistoryDTO.builder()
                                    .id(id)
                                    .buyerId(buyerId)
                                    .build());
    }
}
