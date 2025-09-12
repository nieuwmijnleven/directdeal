package kr.co.directdeal.transactionhistoryservice.domain.object;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a Buy History record.
 * Contains details about a purchased item including buyer, seller,
 * item details, and completion time.
 *
 * @author Cheol Jeon
 */
@Entity
@Table(name = "BUY_HISTORY",
        indexes = {
                @Index(unique = false, columnList = "BUY_ITEM_BUYER_ID, BUY_ITEM_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class BuyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUY_ID")
    private Long id;

    /**
     * Buyer ID (user who purchased the item).
     */
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "BUY_ITEM_BUYER_ID", length = 64, nullable = false)
    private String buyerId;

    /**
     * Item ID that was purchased.
     */
    @NotNull
    @Size(min = 36, max = 36)
    @Column(name = "BUY_ITEM_ID", length = 36, nullable = false)
    private String itemId;

    /**
     * Title of the purchased item.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "BUY_ITEM_TITLE", length = 128, nullable = false)
    private String title;

    /**
     * Category of the purchased item.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "BUY_ITEM_CATEGORY", length = 128, nullable = false)
    private String category;

    /**
     * Target price of the item at the time of purchase.
     */
    @NotNull
    @Positive
    @Column(name = "BUY_ITEM_TARGET_PRICE", nullable = false)
    private long targetPrice;

    /**
     * Main image URL or path of the item.
     */
    @NotNull
    @Size(min = 6, max = 41)
    @Column(name = "BUY_ITEM_MAIN_IMAGE", length = 41, nullable = false)
    private String mainImage;

    /**
     * Seller ID (user who sold the item).
     */
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "BUY_ITEM_SELLER", length = 64, nullable = true)
    private String sellerId;

    /**
     * Completion time of the transaction.
     */
    @NotNull
    @Column(name = "BUY_ITEM_COMPLETION_TIME", nullable = false)
    private Instant completionTime;
}
