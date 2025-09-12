package kr.co.directdeal.saleservice.port.outbound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.object.ItemCategory;

/**
 * Repository interface for {@link ItemCategory}.
 *
 * Provides database operations for item categories, including
 * retrieving root categories (those without a parent).
 *
 * @author Cheol Jeon
 */
@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    /**
     * Finds all item categories that do not have a parent category.
     *
     * @return a list of root item categories
     */
    List<ItemCategory> findAllByParentIsNull();
}
