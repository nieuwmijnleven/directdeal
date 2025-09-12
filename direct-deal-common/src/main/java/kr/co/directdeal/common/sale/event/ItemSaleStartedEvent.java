package kr.co.directdeal.common.sale.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Event indicating that the sale of an item has started.
 *
 * This event is published when the sale process for the specified item begins.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleStartedEvent {

    /**
     * Unique identifier of the item whose sale has started.
     */
    @TargetAggregateIdentifier
    private String id;
}
