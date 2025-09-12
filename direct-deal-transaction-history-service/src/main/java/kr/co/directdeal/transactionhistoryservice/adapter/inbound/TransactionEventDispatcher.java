package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.time.Instant;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.transactionhistoryservice.event.BuyerSetEvent;
import kr.co.directdeal.transactionhistoryservice.application.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.TransactionHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles domain events related to completed sales and buyer assignment.
 * This component reacts to events and updates the transaction and buy history accordingly.
 *
 * - {@link ItemSaleCompletedEvent}: Triggers creation of a transaction record for the seller.
 * - {@link BuyerSetEvent}: Triggers creation of a buy history record for the buyer.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Component
@ProcessingGroup("transactionHistory")
public class TransactionEventDispatcher {

    private final TransactionHistoryService transactionHistoryService;

    private final BuyHistoryService buyHistoryService;

    /**
     * Handles the {@link ItemSaleCompletedEvent} from the domain.
     * Creates a new transaction history entry for the seller.
     *
     * @param event the item sale completion event
     */
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
                        .mainImage(event.getMainImage())
                        .completionTime(Instant.now())
                        .build();

        transactionHistoryService.insert(transactionHistoryDTO);
    }

    /**
     * Handles the {@link BuyerSetEvent} to record the buyer's transaction history.
     *
     * @param event the event containing buyer and transaction information
     */
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
                        .mainImage(dto.getMainImage())
                        .sellerId(dto.getSellerId())
                        .completionTime(dto.getCompletionTime())
                        .build();

        log.debug("[BuyerSetEvent] BuyHistoryDTO => {}", buyHistoryDTO);

        buyHistoryService.insert(buyHistoryDTO);
    }
}
