package kr.co.directdeal.common.sale.command;

import java.time.Instant;
import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Command class for registering a new sale item with all necessary details.
 *
 * Contains information such as owner, title, category, pricing, images, and status.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemRegisterCommand {

    /**
     * Unique identifier for the item.
     */
    @TargetAggregateIdentifier
    private String id;

    /**
     * Owner's identifier (usually user id or email).
     */
    private String ownerId;

    /**
     * Title of the item.
     */
    private String title;

    /**
     * Category of the item.
     */
    private String category;

    /**
     * Target price for the sale.
     */
    private long targetPrice;

    /**
     * Indicates if the item is discountable.
     */
    private boolean discountable;

    /**
     * Detailed description or text about the item.
     */
    private String text;

    /**
     * List of image URLs or identifiers related to the item.
     */
    private List<String> images;

    /**
     * Current status of the sale item.
     */
    private SaleItemStatus status;

    /**
     * The date and time when the item was created.
     */
    private Instant createdDate;

    // Additional audit fields can be added later:
    // private String createdBy;
    // private String lastModifiedBy;
    // private Instant lastModifiedByDate; 
}
