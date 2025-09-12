package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Command class to mark an item sale as complete.
 *
 * Contains essential item information such as id, owner, title, category, and target price.
 * This command is typically used to signal the completion of a sale process.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleCompleteCommand {

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
}
