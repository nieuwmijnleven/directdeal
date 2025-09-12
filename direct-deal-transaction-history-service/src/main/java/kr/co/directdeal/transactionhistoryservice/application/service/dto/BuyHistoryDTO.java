package kr.co.directdeal.transactionhistoryservice.application.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for Buy History.
 *
 * <p>This DTO represents the purchase history details of a buyer,
 * including item information, seller, and completion timestamp.</p>
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BuyHistoryDTO {

    /** Unique identifier for the buy history record */
    private Long id;

    /** Identifier of the buyer */
    private String buyerId;

    /** Identifier of the item purchased */
    private String itemId;

    /** Title of the purchased item */
    private String title;

    /** Category of the purchased item */
    private String category;

    /** Purchase price of the item */
    private long targetPrice;

    /** Main image URL or filename of the purchased item */
    private String mainImage;

    /** Identifier of the seller */
    private String sellerId;

    /** Timestamp when the purchase was completed */
    private Instant completionTime;
}
