package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Command class to stop an ongoing item sale.
 *
 * This command typically triggers the process to stop or pause the sale of the specified item.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleStopCommand {

    /**
     * Unique identifier for the item to stop the sale.
     */
    @TargetAggregateIdentifier
    private String id;
}
