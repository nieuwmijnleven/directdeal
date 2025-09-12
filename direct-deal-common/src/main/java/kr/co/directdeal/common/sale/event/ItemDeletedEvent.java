package kr.co.directdeal.common.sale.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Event indicating that an item has been deleted.
 *
 * This event is typically published when an item is removed from the system.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemDeletedEvent {

    /**
     * Unique identifier of the deleted item.
     */
    @TargetAggregateIdentifier
    private String id;
}
