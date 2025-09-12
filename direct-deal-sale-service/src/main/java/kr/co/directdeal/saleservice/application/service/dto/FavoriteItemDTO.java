package kr.co.directdeal.saleservice.application.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Favorite Item entity.
 *
 * Represents the data related to a user's favorite item, including
 * its identifier, associated user ID, item ID, and the creation timestamp.
 *
 * This DTO is used to transfer favorite item data between layers.
 *
 * @author Cheol Jeon
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavoriteItemDTO {
    /**
     * Unique identifier of the favorite item record.
     */
    private Long id;

    /**
     * The ID of the user who marked the item as favorite.
     */
    private String userId;

    /**
     * The ID of the item marked as favorite.
     */
    private String itemId;

    /**
     * Timestamp when the favorite item record was created.
     */
    private Instant createdDate;
}
