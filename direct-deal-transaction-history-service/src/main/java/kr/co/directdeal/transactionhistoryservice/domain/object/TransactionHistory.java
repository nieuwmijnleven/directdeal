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
 * Entity representing a Transaction History record.
 * Contains details about a transaction including seller, buyer,
 * item details, and completion time.
 *
 * @author Cheol Jeon
 */
@Entity
@Table(name = "TRANSACTION_HISTORY",
        indexes = {
                @Index(unique = false, columnList = "TRANSACTION_ITEM_ID, TRANSACTION_ITEM_SELLER_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private Long id;

    /**
     * Seller ID (user who sold the item).
     */
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "TRANSACTION_ITEM_SELLER_ID", length = 64, nullable = false)
    private String sellerId;

    /**
     * Item ID involved in the transaction.
     */
    @NotNull
    @Size(min = 36, max = 36)
    @Column(name = "TRANSACTION_ITEM_ID", length = 36, nullable = false)
    private String itemId;

    /**
     * Title of the transaction item.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "TRANSACTION_ITEM_TITLE", length = 128, nullable = false)
    private String title;

    /**
     * Category of the transaction item.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "TRANSACTION_ITEM_CATEGORY", length = 128, nullable = false)
    private String category;

    /**
     * Target price of the item at the time of transaction.
     */
    @NotNull
    @Positive
    @Column(name = "TRANSACTION_ITEM_TARGET_PRICE", nullable = false)
    private long targetPrice;

    /**
     * Main image URL or path of the transaction item.
     */
    @NotNull
    @Size(min = 6, max = 41)
    @Column(name = "TRANSACTION_ITEM_MAIN_IMAGE", length = 41, nullable = false)
    private String mainImage;

    /**
     * Buyer ID (user who bought the item). Nullable if buyer not set yet.
     */
    @Size(min = 1, max = 64)
    @Column(name = "TRANSACTION_ITEM_BUYER_ID", length = 64, nullable = true)
    private String buyerId;

    /**
     * Completion time of the transaction.
     */
    @NotNull
    @Column(name = "TRANSACTION_ITEM_COMPLETION_TIME", nullable = false)
    private Instant completionTime;
}
