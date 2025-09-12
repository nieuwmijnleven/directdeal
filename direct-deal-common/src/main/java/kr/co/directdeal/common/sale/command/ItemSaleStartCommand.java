package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Command class to initiate the start of an item sale.
 *
 * This command typically triggers the sale process for the specified item.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleStartCommand {

    /**
     * Unique identifier for the item to start the sale.
     */
    @TargetAggregateIdentifier
    private String id;
}
