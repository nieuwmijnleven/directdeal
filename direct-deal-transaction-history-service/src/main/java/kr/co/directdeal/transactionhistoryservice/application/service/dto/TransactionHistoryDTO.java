package kr.co.directdeal.transactionhistoryservice.application.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for Transaction History.
 *
 * <p>This DTO encapsulates the details of a completed transaction,
 * including item information, seller and buyer details, and completion time.</p>
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransactionHistoryDTO {

    /** Unique identifier for the transaction history record */
    private Long id;

    /** Identifier of the item involved in the transaction */
    private String itemId;

    /** Identifier of the seller */
    private String sellerId;

    /** Title of the transaction item */
    private String title;

    /** Category of the transaction item */
    private String category;

    /** Final transaction price */
    private long targetPrice;

    /** Main image URL or filename of the transaction item */
    private String mainImage;

    /** Identifier of the buyer */
    private String buyerId;

    /** Timestamp when the transaction was completed */
    private Instant completionTime;
}
