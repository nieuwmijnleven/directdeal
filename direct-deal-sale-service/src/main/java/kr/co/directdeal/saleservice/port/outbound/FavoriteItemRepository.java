package kr.co.directdeal.saleservice.port.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.object.FavoriteItem;

/**
 * Repository interface for {@link FavoriteItem}.
 *
 * Provides database access methods for managing favorite items
 * including queries by user ID and item ID.
 *
 * @author Cheol Jeon
 */
@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {

    /**
     * Finds a favorite item by user ID and item ID.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @return an {@link Optional} containing the favorite item if found, or empty otherwise
     */
    Optional<FavoriteItem> findByUserIdAndItemId(String userId, String itemId);

    /**
     * Retrieves all favorite items for a given user.
     *
     * @param userId the user ID
     * @return a list of favorite items associated with the user
     */
    List<FavoriteItem> findAllByUserId(String userId);

    /**
     * Deletes a favorite item by user ID and item ID.
     *
     * @param userId the user ID
     * @param itemId the item ID
     */
    void deleteByUserIdAndItemId(String userId, String itemId);
}
