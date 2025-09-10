package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import java.time.Instant;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.sale.event.ItemDeletedEvent;
import kr.co.directdeal.common.sale.event.ItemRegisteredEvent;
import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStartedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStoppedEvent;
import kr.co.directdeal.common.sale.event.ItemUpdatedEvent;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleItem;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleItemRepository;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessingGroup("saleItem")
public class SaleItemEventDispatcher {
    
    private final SaleItemRepository saleItemRepository;
    
    private final SaleListRepository saleListRepository;

    @EventHandler
    public void on(ItemRegisteredEvent event) {
        log.debug("[{}] Triggered ItemRegisterEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = SaleItem.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .ownerId(event.getOwnerId())
                                .category(event.getCategory())
                                .targetPrice(event.getTargetPrice())
                                .discountable(event.isDiscountable())
                                .text(event.getText())
                                .images(event.getImages())
                                .status(event.getStatus())
                                .createdDate(Instant.now())
                                .build();

        saleItemRepository
            .save(saleItem)
            .subscribe(result -> log.info("saleItem => {}", result));

        SaleList saleList = SaleList.builder()
                                .id(event.getId())
                                .title(event.getTitle())
                                .category(event.getCategory())
                                .targetPrice(event.getTargetPrice())
                                .mainImage(event.getImages().get(0))
                                .status(event.getStatus())
                                .createdDate(Instant.now())
                                .build();

        saleListRepository
            .save(saleList)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    @EventHandler
    public void on(ItemUpdatedEvent event) {
        log.debug("[{}] Triggered ItemUpdatedEvent.", this.getClass().getSimpleName());
        findSaleItemById(event.getId())
            .map(saleItem -> {
                saleItem.setTitle(event.getTitle());
                saleItem.setCategory(event.getCategory());
                saleItem.setTargetPrice(event.getTargetPrice());
                saleItem.setDiscountable(event.isDiscountable());
                saleItem.setText(event.getText());
                saleItem.setImages(event.getImages());
                saleItem.setLastModifiedDate(event.getLastModifiedDate());
                return saleItem;
            })
            .flatMap(saleItemRepository::save)
            .subscribe(result -> log.info("saleItem => {}", result));

        findSaleListById(event.getId())
            .map(saleList -> {
                saleList.setTitle(event.getTitle());
                saleList.setCategory(event.getCategory());
                saleList.setTargetPrice(event.getTargetPrice());
                saleList.setDiscountable(event.isDiscountable());
                saleList.setMainImage(event.getImages().get(0));
                return saleList;
            })
            .flatMap(saleListRepository::save)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    @EventHandler
    public void on(ItemDeletedEvent event) {
        log.debug("[{}] Triggered ItemDeletedEvent.", this.getClass().getSimpleName());
        findSaleItemById(event.getId())
            .map(saleItem -> {
                saleItem.setStatus(SaleItemStatus.DELETED);
                return saleItem;
            })
            .flatMap(saleItemRepository::save)
            .subscribe(result -> log.info("saleItem => {}", result));

        findSaleListById(event.getId())
            .map(saleList -> {
                saleList.setStatus(SaleItemStatus.DELETED);
                return saleList;
            })
            .flatMap(saleListRepository::save)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    @EventHandler
    public void on(ItemSaleStartedEvent event) {
        log.debug("[{}] Triggered ItemSaleStartedEvent.", this.getClass().getSimpleName());
        findSaleItemById(event.getId())
            .map(saleItem -> {
                saleItem.setStatus(SaleItemStatus.SALE);
                return saleItem;
            })
            .flatMap(saleItemRepository::save)
            .subscribe(result -> log.info("saleItem => {}", result));

        findSaleListById(event.getId())
            .map(saleList -> {
                saleList.setStatus(SaleItemStatus.SALE);
                return saleList;
            })
            .flatMap(saleListRepository::save)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    @EventHandler
    public void on(ItemSaleStoppedEvent event) {
        log.debug("[{}] Triggered ItemSaleStoppedEvent.", this.getClass().getSimpleName());
        findSaleItemById(event.getId())
            .map(saleItem -> {
                saleItem.setStatus(SaleItemStatus.STOPPED);
                return saleItem;
            })
            .flatMap(saleItemRepository::save)
            .subscribe(result -> log.info("saleItem => {}", result));

        findSaleListById(event.getId())
            .map(saleList -> {
                saleList.setStatus(SaleItemStatus.STOPPED);
                return saleList;
            })
            .flatMap(saleListRepository::save)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    @EventHandler
    public void on(ItemSaleCompletedEvent event) {
        log.debug("[{}] Triggered ItemSaleCompletedEvent.", this.getClass().getSimpleName());
        findSaleItemById(event.getId())
            .map(saleItem -> {
                saleItem.setStatus(SaleItemStatus.COMPLETED);
                return saleItem;
            })
            .flatMap(saleItemRepository::save)
            .subscribe(result -> log.info("saleItem => {}", result));

        findSaleListById(event.getId())
            .map(saleList -> {
                saleList.setStatus(SaleItemStatus.COMPLETED);
                return saleList;
            })
            .flatMap(saleListRepository::save)
            .subscribe(result -> log.info("saleList => {}", result));
    }

    private Mono<SaleItem> findSaleItemById(String id) {
        return saleItemRepository
                    .findById(id)
                    .switchIfEmpty(Mono.error(() -> SaleItemException.builder()
                                                        .messageKey("salecatalogservice.exception.saleitemeventdispatcher.findsaleitembyid.notfound.message")
                                                        .messageArgs(new String[]{id})
                                                        .build()));
    }

    private Mono<SaleList> findSaleListById(String id) {
        return saleListRepository
                    .findById(id)
                    .switchIfEmpty(Mono.error(() -> SaleListException.builder()
                                                        .messageKey("salecatalogservice.exception.saleitemeventdispatcher.findsalelistbyid.notfound.message")
                                                        .messageArgs(new String[]{id})
                                                        .build()));
    }
}
