package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.time.Instant;

import javax.transaction.Transactional;

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
import kr.co.directdeal.sale.catalogservice.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleItemRepository;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessingGroup("saleItem")
public class SaleItemEventDispatcher {
    
    private final SaleItemRepository saleItemRepository;
    
    private final SaleListRepository saleListRepository;

    @Transactional
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

        saleItemRepository.save(saleItem);
        log.debug("saleItem => " + saleItem.toString()); 

        SaleList saleList = SaleList.builder()
                                        .id(event.getId())
                                        .title(event.getTitle())
                                        .category(event.getCategory())
                                        .targetPrice(event.getTargetPrice())
                                        .mainImage(event.getImages().get(0))
                                        .status(event.getStatus())
                                        .createdDate(Instant.now())
                                        .build();

        saleListRepository.save(saleList);
        log.debug("saleList => " + saleList.toString());
    }

    @Transactional
    @EventHandler
    public void on(ItemUpdatedEvent event) {
        log.debug("[{}] Triggered ItemUpdatedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = findSaleItemById(event.getId());
        saleItem.setTitle(event.getTitle());
        saleItem.setCategory(event.getCategory());
        saleItem.setTargetPrice(event.getTargetPrice());
        saleItem.setDiscountable(event.isDiscountable());
        saleItem.setText(event.getText());
        saleItem.setImages(event.getImages());
        saleItem.setLastModifiedDate(event.getLastModifiedDate());

        // saleItemRepository.save(saleItem);
        // log.debug("saleItem => " + saleItem.toString()); 

        SaleList saleList = findSaleListById(event.getId());
        saleList.setTitle(event.getTitle());
        saleList.setCategory(event.getCategory());
        saleList.setTargetPrice(event.getTargetPrice());
        saleList.setDiscountable(event.isDiscountable());
        saleList.setMainImage(event.getImages().get(0));

        // saleListRepository.save(saleList);
        // log.debug("saleList => " + saleList.toString());
    }

    @Transactional
    @EventHandler
    public void on(ItemDeletedEvent event) {
        log.debug("[{}] Triggered ItemDeletedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = findSaleItemById(event.getId());
        saleItem.setStatus(SaleItemStatus.DELETED);
        // saleItemRepository.save(saleItem);

        SaleList saleList = findSaleListById(event.getId());
        saleList.setStatus(SaleItemStatus.DELETED);
        // saleListRepository.save(saleList);
    }

    @Transactional
    @EventHandler
    public void on(ItemSaleStartedEvent event) {
        log.debug("[{}] Triggered ItemSaleStartedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = findSaleItemById(event.getId());
        saleItem.setStatus(SaleItemStatus.SALE);
        // saleItemRepository.save(saleItem);

        SaleList saleList = findSaleListById(event.getId());
        saleList.setStatus(SaleItemStatus.SALE);
        // saleListRepository.save(saleList);
    }

    @Transactional
    @EventHandler
    public void on(ItemSaleStoppedEvent event) {
        log.debug("[{}] Triggered ItemSaleStoppedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = findSaleItemById(event.getId());
        saleItem.setStatus(SaleItemStatus.STOPPED);
        // saleItemRepository.save(saleItem);

        SaleList saleList = findSaleListById(event.getId());
        saleList.setStatus(SaleItemStatus.STOPPED);
        // saleListRepository.save(saleList);
    }

    @Transactional
    @EventHandler
    public void on(ItemSaleCompletedEvent event) {
        log.debug("[{}] Triggered ItemSaleCompletedEvent.", this.getClass().getSimpleName());
        SaleItem saleItem = findSaleItemById(event.getId());
        saleItem.setStatus(SaleItemStatus.COMPLETED);
        // saleItemRepository.save(saleItem);

        SaleList saleList = findSaleListById(event.getId());
        saleList.setStatus(SaleItemStatus.COMPLETED);
        // saleListRepository.save(saleList);
    }

    private SaleItem findSaleItemById(String id) {
        return saleItemRepository
                    .findById(id)
                    .orElseThrow(() -> SaleItemException.builder()
                                            .messageKey("salecatalogservice.exception.saleitemeventdispatcher.findsaleitembyid.notfound.message")
                                            .messageArgs(new String[]{id})
                                            .build());
    }

    private SaleList findSaleListById(String id) {
        return saleListRepository
                    .findById(id)
                    .orElseThrow(() -> SaleListException.builder()
                                            .messageKey("salecatalogservice.exception.saleitemeventdispatcher.findsalelistbyid.notfound.message")
                                            .messageArgs(new String[]{id})
                                            .build());
    }
}
