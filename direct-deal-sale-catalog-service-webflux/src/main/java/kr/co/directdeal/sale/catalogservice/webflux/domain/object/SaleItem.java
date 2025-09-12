package kr.co.directdeal.sale.catalogservice.webflux.domain.object;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.serialization.DefaultInstantDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a Sale Item document stored in MongoDB.
 * <p>
 * Contains fields related to the sale item such as owner, title, category,
 * pricing, status, and timestamps for creation and last modification.
 * </p>
 * <p>
 * Validation annotations ensure data integrity and constraints on field values.
 * </p>
 *
 * @author Cheol Jeon
 */
@Document("SALE_ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class SaleItem {

    /**
     * Unique identifier for the sale item.
     * Must be exactly 36 characters long.
     */
    @NotNull
    @Size(min = 36, max = 36)
    @Id
    private String id;

    /**
     * Identifier of the owner of this sale item.
     * Length must be between 1 and 64 characters.
     */
    @NotNull
    @Size(min = 1, max = 64)
    private String ownerId;

    /**
     * Title of the sale item.
     * Length must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String title;

    /**
     * Category name to which this sale item belongs.
     * Length must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String category;

    /**
     * Target price for the sale item.
     * Must be a positive long value.
     */
    @NotNull
    @Positive
    private long targetPrice;

    /**
     * Whether the item is eligible for discount.
     */
    private boolean discountable;

    /**
     * Description text for the sale item.
     * Length must be between 1 and 1024 characters.
     */
    @NotNull
    @Size(min = 1, max = 1024)
    private String text;

    /**
     * List of image URLs related to the sale item.
     */
    private List<String> images;

    /**
     * Current status of the sale item.
     */
    @NotNull
    private SaleItemStatus status;

    // Uncomment if createdBy tracking is needed
    // @CreatedBy
    // private String createdBy;

    /**
     * Timestamp of when the sale item was created.
     * Automatically populated.
     */
    @CreatedDate
    private Instant createdDate;

    // Uncomment if lastModifiedBy tracking is needed
    // @LastModifiedBy
    // private String lastModifiedBy;

    /**
     * Timestamp of the last modification on the sale item.
     * Automatically updated.
     */
    @LastModifiedDate
    private Instant lastModifiedDate;
}
