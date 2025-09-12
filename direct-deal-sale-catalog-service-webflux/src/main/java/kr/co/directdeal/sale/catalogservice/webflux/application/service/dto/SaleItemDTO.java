package kr.co.directdeal.sale.catalogservice.webflux.application.service.dto;

import java.time.Instant;
import java.util.List;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Sale Item entity.
 * <p>
 * Represents the details of a sale item including ownership, pricing, status, and timestamps.
 * </p>
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleItemDTO {

    /**
     * Unique identifier of the sale item.
     */
    private String id;

    /**
     * Identifier of the owner (seller) of the item.
     */
    private String ownerId;

    /**
     * Title or name of the sale item.
     */
    private String title;

    /**
     * Category to which the item belongs.
     */
    private String category;

    /**
     * Target price set for the sale item.
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
     * List of image URLs related to the item.
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

    /**
     * The date and time when the item was last modified.
     */
    private Instant lastModifiedDate;
}
