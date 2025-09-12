package kr.co.directdeal.saleservice.domain.object;

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

/**
 * Aggregate representing a Sale Item in the system.
 *
 * Handles commands related to item lifecycle such as registration,
 * update, deletion, sale start/stop, and sale completion by applying
 * corresponding domain events.
 *
 * Event sourcing handlers update the aggregate's state in response
 * to these events.
 *
 * @author Cheol Jeon
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Aggregate
public class SaleItemAggregate {

    /**
     * Aggregate identifier for the Sale Item.
     */
    @AggregateIdentifier
    private String id;

    /**
     * The owner user ID of this sale item.
     */
    private String ownerId;

    /**
     * Title of the sale item.
     */
    private String title;

    /**
     * Category identifier or name for the sale item.
     */
    private String category;

    /**
     * Target price of the sale item.
     */
    private long targetPrice;

    /**
     * Flag indicating if the item is discountable.
     */
    private boolean discountable;

    /**
     * Description or text content about the sale item.
     */
    private String text;

    /**
     * List of image URLs or identifiers related to the sale item.
     */
    private List<String> images;

    /**
     * Current status of the sale item.
     */
    private SaleItemStatus status;

    // private String createdBy;

    /**
     * Timestamp when the item was created.
     */
    private Instant createdDate;

    // private String lastModifiedBy;

    /**
     * Timestamp when the item was last modified.
     */
    private Instant lastModifiedDate;

    /**
     * Command handler to register a new sale item.
     * Validates target price and applies an ItemRegisteredEvent.
     *
     * @param cmd the ItemRegisterCommand containing registration details
     * @throws IllegalArgumentException if target price is zero or less
     */
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

    /**
     * Command handler to update an existing sale item.
     * Validates target price and ownership before applying ItemUpdatedEvent.
     *
     * @param cmd the ItemUpdateCommand containing update details
     * @throws IllegalArgumentException if target price is zero or less
     * @throws SaleItemException if current user is not the owner of the item
     */
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

    /**
     * Command handler to delete the sale item.
     * Applies an ItemDeletedEvent.
     *
     * @param cmd the ItemDeleteCommand
     */
    @CommandHandler
    public void handle(ItemDeleteCommand cmd) {
        log.debug("call {}.ItemDeleteCommand", this.getClass().getSimpleName());
        AggregateLifecycle.apply(ItemDeletedEvent.builder()
                .id(cmd.getId())
                .build());
    }

    /**
     * Command handler to start the sale of the item.
     * Applies an ItemSaleStartedEvent.
     *
     * @param cmd the ItemSaleStartCommand
     */
    @CommandHandler
    public void handle(ItemSaleStartCommand cmd) {
        AggregateLifecycle.apply(ItemSaleStartedEvent.builder()
                .id(cmd.getId())
                .build());
    }

    /**
     * Command handler to stop the sale of the item.
     * Applies an ItemSaleStoppedEvent.
     *
     * @param cmd the ItemSaleStopCommand
     */
    @CommandHandler
    public void handle(ItemSaleStopCommand cmd) {
        AggregateLifecycle.apply(ItemSaleStoppedEvent.builder()
                .id(cmd.getId())
                .build());
    }

    /**
     * Command handler to mark the sale as complete.
     * Applies an ItemSaleCompletedEvent with key item details.
     *
     * @param cmd the ItemSaleCompleteCommand
     */
    @CommandHandler
    public void handle(ItemSaleCompleteCommand cmd) {
        AggregateLifecycle.apply(ItemSaleCompletedEvent.builder()
                .id(this.getId())
                .ownerId(this.getOwnerId())
                .title(this.getTitle())
                .category(this.getCategory())
                .targetPrice(this.getTargetPrice())
                .mainImage(this.getImages().get(0))
                .build());

        log.debug("ItemSaleCompletedEvent: {}", this);
    }

    /**
     * Event sourcing handler for ItemRegisteredEvent.
     * Updates aggregate state with registration data.
     *
     * @param event the ItemRegisteredEvent
     */
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

    /**
     * Event sourcing handler for ItemUpdatedEvent.
     * Updates aggregate state with new item data.
     *
     * @param event the ItemUpdatedEvent
     */
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

    /**
     * Event sourcing handler for ItemDeletedEvent.
     * Marks the item as deleted.
     *
     * @param event the ItemDeletedEvent
     */
    @EventSourcingHandler
    public void on(ItemDeletedEvent event) {
        log.debug("call {}.ItemDeletedEvent", this.getClass().getSimpleName());
        this.id = event.getId();
        this.status = SaleItemStatus.DELETED;
    }

    /**
     * Event sourcing handler for ItemSaleStartedEvent.
     * Updates status to SALE.
     *
     * @param event the ItemSaleStartedEvent
     */
    @EventSourcingHandler
    public void on(ItemSaleStartedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.SALE;
    }

    /**
     * Event sourcing handler for ItemSaleStoppedEvent.
     * Updates status to STOPPED.
     *
     * @param event the ItemSaleStoppedEvent
     */
    @EventSourcingHandler
    public void on(ItemSaleStoppedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.STOPPED;
    }

    /**
     * Event sourcing handler for ItemSaleCompletedEvent.
     * Updates status to COMPLETED.
     *
     * @param event the ItemSaleCompletedEvent
     */
    @EventSourcingHandler
    public void on(ItemSaleCompletedEvent event) {
        this.id = event.getId();
        this.status = SaleItemStatus.COMPLETED;
    }
}
