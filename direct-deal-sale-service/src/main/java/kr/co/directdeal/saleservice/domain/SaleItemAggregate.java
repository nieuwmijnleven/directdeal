package kr.co.directdeal.saleservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import kr.co.directdeal.common.sale.command.ItemDeleteCommand;
import kr.co.directdeal.common.sale.command.ItemRegisterCommand;
import kr.co.directdeal.common.sale.command.ItemSaleCompleteCommand;
import kr.co.directdeal.common.sale.command.ItemSaleStartCommand;
import kr.co.directdeal.common.sale.command.ItemSaleStopCommand;
import kr.co.directdeal.common.sale.command.ItemUpdateCommand;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.sale.event.ItemDeletedEvent;
import kr.co.directdeal.common.sale.event.ItemRegisteredEvent;
import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStartedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStoppedEvent;
import kr.co.directdeal.common.sale.event.ItemUpdatedEvent;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.exception.SaleItemException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Aggregate
public class SaleItemAggregate {
    @AggregateIdentifier
    private String id;

    private String ownerId;
    
    private String title;
    
    private String category;
    
    private long targetPrice;

    private boolean discountable;
    
    private String text;
    
    private List<String> images;
    
    private SaleItemStatus status;
        
    // private String createdBy;
    
    private Instant createdDate; 
    
    // private String lastModifiedBy;
    
    private Instant lastModifiedDate; 

    @CommandHandler
    public SaleItemAggregate(ItemRegisterCommand cmd) {
        if (cmd.getTargetPrice() <= 0) {
            throw new IllegalArgumentException("target price <= 0");
        }

        log.debug("id => " + cmd.getId());
        AggregateLifecycle.apply(ItemRegisteredEvent.builder()
                                    .id(cmd.getId())
                                    .ownerId(cmd.getOwnerId())
                                    .title(cmd.getTitle())
                                    .category(cmd.getCategory())
                                    .targetPrice(cmd.getTargetPrice())
                                    .discountable(cmd.isDiscountable())
                                    .text(cmd.getText())
                                    .images(cmd.getImages())
                                    .status(cmd.getStatus())
                                    .createdDate(Instant.now())
                                    .build());      
    }

    @CommandHandler
    public void handle(ItemUpdateCommand cmd) {
        if (cmd.getTargetPrice() <= 0) {
            throw new IllegalArgumentException("target price <= 0");
        }

        String userId = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(userId, this.getOwnerId()))
            throw SaleItemException.builder()
                        .messageKey("saleservice.exception.saleitemaggregate.itemupdatecommand.notthesame.onwerid.message")
                        .messageArgs(new String[]{userId, this.getOwnerId()})
                        .build();

        log.debug("call {}.ItemUpdateCommand", this.getClass().getSimpleName());
        AggregateLifecycle.apply(ItemUpdatedEvent.builder()
                                    .id(cmd.getId())
                                    // .ownerId(cmd.getOwnerId())
                                    .title(cmd.getTitle())
                                    .category(cmd.getCategory())
                                    .targetPrice(cmd.getTargetPrice())
                                    .discountable(cmd.isDiscountable())
                                    .text(cmd.getText())
                                    .images(cmd.getImages())
                                    //.status(SaleItemStatus.SALE)
                                    .lastModifiedDate(cmd.getLastModifiedDate())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemDeleteCommand cmd) {
        log.debug("call {}.ItemDeleteCommand", this.getClass().getSimpleName());
        AggregateLifecycle.apply(ItemDeletedEvent.builder()
                                    .id(cmd.getId())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemSaleStartCommand cmd) {
        AggregateLifecycle.apply(ItemSaleStartedEvent.builder()
                                    .id(cmd.getId())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemSaleStopCommand cmd) {
        AggregateLifecycle.apply(ItemSaleStoppedEvent.builder()
                                    .id(cmd.getId())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemSaleCompleteCommand cmd) {
        AggregateLifecycle.apply(ItemSaleCompletedEvent.builder()
                                    .id(this.getId())
                                    .ownerId(this.getOwnerId())
                                    .title(this.getTitle())
                                    .category(this.getCategory())
                                    .targetPrice(this.getTargetPrice())
                                    .build());

        log.debug("ItemSaleCompletedEvent: {}", this);
    }

    
    @EventSourcingHandler
    public void on(ItemRegisteredEvent event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.ownerId = event.getOwnerId();
        this.category = event.getCategory();
        this.targetPrice = event.getTargetPrice();
        this.discountable = event.isDiscountable();
        this.text = event.getText();
        this.images = event.getImages();
        this.status = event.getStatus();
        this.createdDate = event.getCreatedDate();
    }

    @EventSourcingHandler
    public void on(ItemUpdatedEvent event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.category = event.getCategory();
        this.targetPrice = event.getTargetPrice();
        this.discountable = event.isDiscountable();
        this.text = event.getText();
        this.images = event.getImages();
        this.status = event.getStatus();
        this.lastModifiedDate= event.getLastModifiedDate();
    }
    
    @EventSourcingHandler
    public void on(ItemDeletedEvent event) {
        log.debug("call {}.ItemDeletedEvent", this.getClass().getSimpleName());
        this.id = event.getId();
        this.status = SaleItemStatus.DELETED;
    }
    
    @EventSourcingHandler
    public void on(ItemSaleStartedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.SALE;
    }

    
    @EventSourcingHandler
    public void on(ItemSaleStoppedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.STOPPED;
    }

    @EventSourcingHandler
    public void on(ItemSaleCompletedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.COMPLETED;
    }
}
