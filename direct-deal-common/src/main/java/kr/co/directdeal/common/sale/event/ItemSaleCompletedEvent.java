package kr.co.directdeal.common.sale.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Event representing the completion of an item sale.
 *
 * This event is typically published when the sale of an item is finalized.
 * It contains key details about the sold item.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleCompletedEvent {

    /**
     * Unique identifier of the item whose sale has been completed.
     */
    @TargetAggregateIdentifier
    private String id;

    /**
     * Identifier of the owner of the item.
     */
    private String ownerId;

    /**
     * Title or name of the item.
     */
    private String title;

    /**
     * Category to which the item belongs.
     */
    private String category;

    /**
     * The target price at which the item was sold.
     */
    private long targetPrice;

    /**
     * The main image URL representing the item.
     */
    private String mainImage;
}
