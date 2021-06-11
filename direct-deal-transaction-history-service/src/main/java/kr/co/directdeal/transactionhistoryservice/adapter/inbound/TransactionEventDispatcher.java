package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.time.Instant;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.transactionhistoryservice.event.BuyerSetEvent;
import kr.co.directdeal.transactionhistoryservice.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.service.TransactionHistoryService;
import kr.co.directdeal.transactionhistoryservice.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessingGroup("transactionHistory")
public class TransactionEventDispatcher {

    private final TransactionHistoryService transactionHistoryService;

    private final BuyHistoryService buyHistoryService;
    
    @EventHandler
    public void on(ItemSaleCompletedEvent event) {
        TransactionHistoryDTO transactionHistoryDTO = 
            TransactionHistoryDTO.builder()
                .itemId(event.getId())
                .sellerId(event.getOwnerId())
                //.sellerId("seller@directdeal.co.kr")
                .title(event.getTitle())
                .category(event.getCategory())
                .targetPrice(event.getTargetPrice())
                .completionTime(Instant.now())
                .build();

        transactionHistoryService.insert(transactionHistoryDTO);
    }

    @EventListener
    public void on(BuyerSetEvent event) {
        TransactionHistoryDTO dto = event.getTransactionHistoryDTO();
        BuyHistoryDTO buyHistoryDTO = 
            BuyHistoryDTO.builder()
                .buyerId(dto.getBuyerId())
                .itemId(dto.getItemId())
                .title(dto.getTitle())
                .category(dto.getCategory())
                .targetPrice(dto.getTargetPrice())
                .sellerId(dto.getSellerId())
                .completionTime(dto.getCompletionTime())
                .build();
                
        log.debug("[BuyerSetEvent] BuyHistoryDTO => {}", buyHistoryDTO);

        buyHistoryService.insert(buyHistoryDTO);
    }
}
