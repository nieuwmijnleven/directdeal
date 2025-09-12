package kr.co.directdeal.sale.catalogservice.webflux.application.service.dto;

import java.time.Instant;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Sale List entity.
 * <p>
 * Represents summarized information about a sale item for list views,
 * including title, category, price, main image, status, and creation time.
 * </p>
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleListDTO {

    /**
     * Unique identifier of the sale list item.
     */
    private String id;

    /**
     * Title or name of the sale item.
     */
    private String title;

    /**
     * Category the item belongs to.
     */
    private String category;

    /**
     * Target price for the sale item.
     */
    private long targetPrice;

    /**
     * URL or path to the main image representing the item.
     */
    private String mainImage;

    /**
     * Current sale status of the item.
     */
    private SaleItemStatus status;

    /**
     * The creation date and time of the sale list entry.
     */
    private Instant createdDate;
}
