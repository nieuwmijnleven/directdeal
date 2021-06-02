package kr.co.directdeal.saleservice.query;

import org.axonframework.eventhandling.AllowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.sale.event.ItemDeletedEvent;
import kr.co.directdeal.common.sale.event.ItemRegisteredEvent;
import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStartedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStoppedEvent;
import kr.co.directdeal.common.sale.event.ItemUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SaleItemProjection {
    
    private final SaleItemRepository saleItemRepository;

    @AllowReplay
    @EventHandler
    public void on(ItemRegisteredEvent event) {
        log.debug("[{}] Triggered ItemRegisterEvent.", this.getClass().getSimpleName());

        SaleItem saleItem = SaleItem.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .category(event.getCategory())
                                .targetPrice(event.getTargetPrice())
                                .text(event.getText())
                                .imagePath(event.getImagePath())
                                .status(event.getStatus())
                                .build();

        saleItemRepository.save(saleItem);
    }

    @AllowReplay
    @EventHandler
    public void on(ItemUpdatedEvent event) {
        log.debug("[{}] Triggered ItemUpdatedEvent.", this.getClass().getSimpleName());

        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        
        saleItem.setTitle(event.getTitle());
        saleItem.setCategory(event.getCategory());
        saleItem.setTargetPrice(event.getTargetPrice());
        saleItem.setText(event.getText());
        saleItem.setImagePath(event.getImagePath());
    }

    @AllowReplay
    @EventHandler
    public void on(ItemDeletedEvent event) {
        log.debug("[{}] Triggered ItemDeletedEvent.", this.getClass().getSimpleName());
        
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);

        saleItem.setStatus("DELETED");
    }

    @AllowReplay
    @EventHandler
    public void on(ItemSaleStartedEvent event) {
        log.debug("[{}] Triggered ItemSaleStartedEvent.", this.getClass().getSimpleName());
        
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);

        saleItem.setStatus("SALE");
    }

    @AllowReplay
    @EventHandler
    public void on(ItemSaleStoppedEvent event) {
        log.debug("[{}] Triggered ItemSaleStoppedEvent.", this.getClass().getSimpleName());
        
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);

        saleItem.setStatus("STOPPED");
    }

    @AllowReplay
    @EventHandler
    public void on(ItemSaleCompletedEvent event) {
        log.debug("[{}] Triggered ItemSaleCompletedEvent.", this.getClass().getSimpleName());
        
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);

        saleItem.setStatus("COMPLETED");
    }

    @ResetHandler
    public void resetSaleItemRepository() {
        saleItemRepository.deleteAll();
    }

    @QueryHandler
    public SaleItem on(SaleItemQuery query) {
        return saleItemRepository.findById(query.getId())
                    .orElseThrow(IllegalStateException::new);
    }
}
