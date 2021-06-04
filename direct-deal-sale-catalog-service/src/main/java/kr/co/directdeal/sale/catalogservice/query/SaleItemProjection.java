package kr.co.directdeal.sale.catalogservice.query;

import java.time.Instant;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
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
    
    private final SaleListItemRepository saleListItemRepository;

    @EventHandler
    public void on(ItemRegisteredEvent event) {
        log.debug("[{}] Triggered ItemRegisterEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = SaleItem.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .category(event.getCategory())
                                .targetPrice(event.getTargetPrice())
                                .text(event.getText())
                                .images(event.getImages())
                                .status(event.getStatus())
                                .createdDate(Instant.now())
                                .build();

        saleItemRepository.save(saleItem);
        log.debug("saleItem => " + saleItem.toString()); 

        SaleListItem saleListItem = SaleListItem.builder()
                                        .id(event.getId())
                                        .title(event.getTitle())
                                        .category(event.getCategory())
                                        .targetPrice(event.getTargetPrice())
                                        .mainImage(event.getImages().get(0))
                                        .status(event.getStatus())
                                        .createdDate(Instant.now())
                                        .build();

        saleListItemRepository.save(saleListItem);
        log.debug("saleListItem => " + saleListItem.toString());
    }

    @EventHandler
    public void on(ItemUpdatedEvent event) {
        log.debug("[{}] Triggered ItemUpdatedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        
        saleItem.setTitle(event.getTitle());
        saleItem.setCategory(event.getCategory());
        saleItem.setTargetPrice(event.getTargetPrice());
        saleItem.setText(event.getText());
        saleItem.setImages(event.getImages());

        SaleListItem saleListItem = saleListItemRepository.findById(event.getId())
                                        .orElseThrow(IllegalStateException::new);
        
        saleListItem.setTitle(event.getTitle());
        saleListItem.setCategory(event.getCategory());
        saleListItem.setTargetPrice(event.getTargetPrice());
        saleListItem.setMainImage(event.getImages().get(0));
    }

    @EventHandler
    public void on(ItemDeletedEvent event) {
        log.debug("[{}] Triggered ItemDeletedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        saleItem.setStatus(SaleItemStatus.DELETED);

        SaleListItem saleListItem = saleListItemRepository.findById(event.getId())
                                        .orElseThrow(IllegalStateException::new);
        saleListItem.setStatus(SaleItemStatus.DELETED);
    }

    @EventHandler
    public void on(ItemSaleStartedEvent event) {
        log.debug("[{}] Triggered ItemSaleStartedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        saleItem.setStatus(SaleItemStatus.SALE);

        SaleListItem saleListItem = saleListItemRepository.findById(event.getId())
                                        .orElseThrow(IllegalStateException::new);
        saleListItem.setStatus(SaleItemStatus.SALE);
    }

    @EventHandler
    public void on(ItemSaleStoppedEvent event) {
        log.debug("[{}] Triggered ItemSaleStoppedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        saleItem.setStatus(SaleItemStatus.STOPPED);

        SaleListItem saleListItem = saleListItemRepository.findById(event.getId())
                                        .orElseThrow(IllegalStateException::new);
        saleListItem.setStatus(SaleItemStatus.STOPPED);
    }

    @EventHandler
    public void on(ItemSaleCompletedEvent event) {
        log.debug("[{}] Triggered ItemSaleCompletedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = saleItemRepository.findById(event.getId())
                                .orElseThrow(IllegalStateException::new);
        saleItem.setStatus(SaleItemStatus.COMPLETED);

        SaleListItem saleListItem = saleListItemRepository.findById(event.getId())
                                        .orElseThrow(IllegalStateException::new);
        saleListItem.setStatus(SaleItemStatus.COMPLETED);
    }

    @QueryHandler
    public SaleItem on(SaleItemQuery query) {
        return saleItemRepository
                    .findById(query.getId())
                    .orElseThrow(IllegalStateException::new);
    }

    @QueryHandler
    public SaleListItem on(SaleListQuery query) {
        return saleListItemRepository
                    .findById(query.getId())
                    .orElseThrow(IllegalStateException::new);
    }
}
