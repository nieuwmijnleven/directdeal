package kr.co.directdeal.sale.catalogservice.webflux.domain.object;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a summarized sale list item for display purposes.
 * <p>
 * Contains essential information about the sale item including title,
 * category, main image, price, status, and timestamps.
 * </p>
 * <p>
 * Includes validation constraints to ensure data integrity.
 * </p>
 *
 * @author Cheol Jeon
 */
@Document("SALE_LIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleList {

    /**
     * Unique identifier for the sale list item.
     * Must be exactly 36 characters.
     */
    @NotNull
    @Size(min = 36, max = 36)
    @Id
    private String id;

    /**
     * Title of the sale list item.
     * Length must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String title;

    /**
     * Category of the sale list item.
     * Length must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String category;

    /**
     * Target price of the item.
     * Must be a positive value.
     */
    @NotNull
    @Positive
    private long targetPrice;

    /**
     * Whether the item is discountable.
     */
    private boolean discountable;

    /**
     * URL of the main image representing the sale list item.
     * Length must be between 6 and 41 characters.
     */
    @NotNull
    @Size(min = 6, max = 41)
    private String mainImage;

    /**
     * Status of the sale list item.
     */
    @NotNull
    private SaleItemStatus status;

    /**
     * Timestamp when the sale list item was created.
     * Automatically populated.
     */
    @CreatedDate
    private Instant createdDate;

    /**
     * Timestamp when the sale list item was last modified.
     * Automatically updated.
     */
    @LastModifiedDate
    private Instant lastModifiedDate;

    /**
     * Creates a copy of the provided SaleList instance.
     *
     * @param another the SaleList instance to copy from
     * @return a new SaleList instance with copied field values
     */
    public static SaleList copyOf(SaleList another) {
        return SaleList.builder()
                .id(another.getId())
                .title(another.getTitle())
                .category(another.getCategory())
                .targetPrice(another.getTargetPrice())
                .discountable(another.isDiscountable())
                .mainImage(another.getMainImage())
                .status(another.getStatus())
                .createdDate(another.getCreatedDate())
                .lastModifiedDate(another.getLastModifiedDate())
                .build();
    }
}
