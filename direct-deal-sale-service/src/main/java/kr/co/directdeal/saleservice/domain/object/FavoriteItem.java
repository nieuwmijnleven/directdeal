package kr.co.directdeal.saleservice.domain.object;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a user's favorite item.
 *
 * This entity maps to the FAVORITE_ITEM table and stores information
 * about the items favorited by users.
 *
 * @author Cheol Jeon
 */
@Entity
@Table(name = "FAVORITE_ITEM",
        indexes = {
                @Index(unique = true, columnList = "USER_ID, ITEM_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class FavoriteItem {
    /**
     * Unique identifier for the favorite item record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAVORITE_ITEM_ID")
    private Long id;

    /**
     * Identifier of the user who favorited the item.
     */
    @NotNull
    @Column(name = "USER_ID", length = 64, nullable = false)
    private String userId;

    /**
     * Identifier of the item favorited by the user.
     */
    @NotNull
    @Column(name = "ITEM_ID", length = 36, nullable = false)
    private String itemId;

    /**
     * Timestamp when the favorite record was created.
     */
    @CreatedDate
    @Column(name = "CREATED_DATE")
    private Instant createdDate;
}
