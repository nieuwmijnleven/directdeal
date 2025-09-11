package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.application.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
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
        String userId = SecurityUtils.getCurrentUserLogin();
        return buyHistoryService.list(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        String buyerId = SecurityUtils.getCurrentUserLogin();
        buyHistoryService.delete(BuyHistoryDTO.builder()
                                    .id(id)
                                    .buyerId(buyerId)
                                    .build());
    }
}
