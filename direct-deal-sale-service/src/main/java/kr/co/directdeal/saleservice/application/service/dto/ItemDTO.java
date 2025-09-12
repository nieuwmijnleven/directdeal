package kr.co.directdeal.saleservice.application.service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing an item.
 * Contains details such as owner, title, category, price, description, images, and status.
 * Validation annotations ensure data integrity.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    /**
     * Unique identifier of the item.
     */
    private String id;

    /**
     * Owner ID of the item.
     * Must be between 5 and 64 characters in length.
     */
    @Size(min = 5, max = 64)
    private String ownerId;

    /**
     * Title of the item.
     * Cannot be null and must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String title;

    /**
     * Category to which the item belongs.
     * Cannot be null and must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String category;

    /**
     * Target price of the item.
     * Must be a positive number and cannot be null.
     */
    @NotNull
    @Positive
    private long targetPrice;

    /**
     * Indicates whether the item is discountable.
     */
    private boolean discountable;

    /**
     * Description text of the item.
     * Cannot be null and must be between 1 and 1024 characters.
     */
    @NotNull
    @Size(min = 1, max = 1024)
    private String text;

    /**
     * List of image URLs associated with the item.
     * Defaults to an empty list.
     */
    @Builder.Default
    private List<String> images = new ArrayList<>();

    /**
     * Current status of the sale item.
     */
    private SaleItemStatus status;

    // private String createdBy;

    /**
     * Timestamp when the item was created.
     */
    private Instant createdDate;

    // private String lastModifiedBy;

    /**
     * Timestamp when the item was last modified.
     */
    private Instant lastModifiedByDate;
}
