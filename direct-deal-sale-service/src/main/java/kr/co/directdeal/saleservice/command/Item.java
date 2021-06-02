package kr.co.directdeal.saleservice.command;

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
import kr.co.directdeal.common.sale.event.ItemDeletedEvent;
import kr.co.directdeal.common.sale.event.ItemRegisteredEvent;
import kr.co.directdeal.common.sale.event.ItemSaleCompletedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStartedEvent;
import kr.co.directdeal.common.sale.event.ItemSaleStoppedEvent;
import kr.co.directdeal.common.sale.event.ItemUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Aggregate
@AllArgsConstructor
@NoArgsConstructor
@Aggregate
public class Item {
    @AggregateIdentifier
    private String id;
    // private String ownerId;
    private String title;
    private String category;
    private long targetPrice;
    private String text;
    private String imagePath;
    private String status;
    // private String createdBy;
    // private Instant createdDate; 
    // private String lastModifiedBy;
    // private Instant lastModifiedByDate; 

    @CommandHandler
    public Item(ItemRegisterCommand cmd) {
        if (cmd.getTargetPrice() <= 0) {
            throw new IllegalArgumentException("target price <= 0");
        }

        log.debug("id => " + cmd.getId());
        AggregateLifecycle.apply(ItemRegisteredEvent.builder()
                                    .id(cmd.getId())
                                    .title(cmd.getTitle())
                                    .category(cmd.getCategory())
                                    .targetPrice(cmd.getTargetPrice())
                                    .text(cmd.getText())
                                    .imagePath(cmd.getImagePath())
                                    .status(cmd.getStatus())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemUpdateCommand cmd) {
        if (cmd.getTargetPrice() <= 0) {
            throw new IllegalArgumentException("target price <= 0");
        }

        log.debug("call {}.ItemUpdateCommand", this.getClass().getSimpleName());

        AggregateLifecycle.apply(ItemUpdatedEvent.builder()
                                    .id(cmd.getId())
                                    .title(cmd.getTitle())
                                    .category(cmd.getCategory())
                                    .targetPrice(cmd.getTargetPrice())
                                    .text(cmd.getText())
                                    .imagePath(cmd.getImagePath())
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
                                    .status(cmd.getStatus())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemSaleStopCommand cmd) {
        AggregateLifecycle.apply(ItemSaleStoppedEvent.builder()
                                    .id(cmd.getId())
                                    .status(cmd.getStatus())
                                    .build());
    }

    @CommandHandler
    public void handle(ItemSaleCompleteCommand cmd) {
        AggregateLifecycle.apply(ItemSaleCompletedEvent.builder()
                                    .id(cmd.getId())
                                    .status(cmd.getStatus())
                                    .build());
    }

    
    @EventSourcingHandler
    public void on(ItemRegisteredEvent event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.category = event.getCategory();
        this.targetPrice = event.getTargetPrice();
        this.text = event.getText();
        this.imagePath = event.getImagePath();
        this.status = event.getStatus();
    }

    
    @EventSourcingHandler
    public void on(ItemUpdatedEvent event) {
        log.debug("ItemUpdatedEvent => " + event.getId());
        this.id = event.getId();
        this.title = event.getTitle();
        this.category = event.getCategory();
        this.targetPrice = event.getTargetPrice();
        this.text = event.getText();
        this.imagePath = event.getImagePath();
        this.status = event.getStatus();
    }
    
    
    @EventSourcingHandler
    public void on(ItemDeletedEvent event) {
        log.debug("call {}.ItemDeletedEvent", this.getClass().getSimpleName());
        this.id = event.getId();
        this.status = "DELETED";
    }

    
    @EventSourcingHandler
    public void on(ItemSaleStartedEvent event) {
        this.id = event.getId();
        this.status = "SALE";
    }

    
    @EventSourcingHandler
    public void on(ItemSaleStoppedEvent event) {
        this.id = event.getId();
        this.status = "STOPPED";
    }

    
    @EventSourcingHandler
    public void on(ItemSaleCompletedEvent event) {
        this.id = event.getId();
        this.status = "COMPLETED";
    }
}
