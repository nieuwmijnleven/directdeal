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
 * Command class to update the details of an existing sale item.
 * <p>
 * This command carries updated information such as title, category, price,
 * discount eligibility, description text, images, and the current sale status.
 * </p>
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemUpdateCommand {

    /**
     * Unique identifier for the item to be updated.
     */
    @TargetAggregateIdentifier
    private String id;

    /**
     * Owner's user ID who owns the item.
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
     * Target price of the item.
     */
    private long targetPrice;

    /**
     * Whether the item is discountable or not.
     */
    private boolean discountable;

    /**
     * Detailed description text of the item.
     */
    private String text;

    /**
     * List of image URLs or identifiers associated with the item.
     */
    private List<String> images;

    /**
     * Current status of the sale item.
     */
    private SaleItemStatus status;

    /**
     * The timestamp when the item was last modified.
     */
    private Instant lastModifiedDate;
}
