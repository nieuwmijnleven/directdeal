package kr.co.directdeal.common.sale.event;

import java.time.Instant;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Event representing the registration of a new item.
 *
 * This event is typically published when an item is newly registered in the system.
 *
 * It contains all relevant details about the item at the time of registration.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemRegisteredEvent {

    /**
     * Unique identifier of the registered item.
     */
    @TargetAggregateIdentifier
    private String id;

    /**
     * Identifier of the item owner.
     */
    private String ownerId;

    /**
     * Title or name of the item.
     */
    private String title;

    /**
     * Category the item belongs to.
     */
    private String category;

    /**
     * Target price for the item sale.
     */
    private long targetPrice;

    /**
     * Indicates if the item is discountable.
     */
    private boolean discountable;

    /**
     * Description or details about the item.
     */
    private String text;

    /**
     * List of image URLs associated with the item.
     */
    private List<String> images;

    /**
     * Current status of the sale item.
     */
    private SaleItemStatus status;

    // private String createdBy;

    /**
     * The date and time when the item was created.
     */
    private Instant createdDate;

    // private String lastModifiedBy;

    // private Instant lastModifiedByDate; 
}
