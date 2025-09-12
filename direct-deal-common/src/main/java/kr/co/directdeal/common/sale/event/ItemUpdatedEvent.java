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
 * Event representing the update of an item.
 *
 * This event is published when an item’s details are updated, including title, category,
 * price, discountability, description, images, status, and modification dates.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemUpdatedEvent {
    /**
     * Unique identifier of the item being updated.
     */
    @TargetAggregateIdentifier
    private String id;

    private String ownerId;

    private String title;

    private String category;

    private long targetPrice;

    private boolean discountable;

    private String text;

    private List<String> images;

    private SaleItemStatus status;

    // private String createdBy;

    private Instant createdDate;

    // private String lastModifiedBy;

    private Instant lastModifiedDate;
}
