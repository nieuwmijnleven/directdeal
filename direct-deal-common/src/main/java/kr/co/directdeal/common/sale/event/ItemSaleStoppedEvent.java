package kr.co.directdeal.common.sale.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Event indicating that the sale of an item has been stopped.
 *
 * This event is published when the sale process for the specified item is halted.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleStoppedEvent {

    /**
     * Unique identifier of the item whose sale has been stopped.
     */
    @TargetAggregateIdentifier
    private String id;
}
