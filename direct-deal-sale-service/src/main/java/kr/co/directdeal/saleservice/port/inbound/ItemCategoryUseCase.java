package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case interface for managing Item Categories.
 *
 * Provides methods to list, insert, update, and delete item categories.
 *
 * @author Cheol Jeon
 */
public interface ItemCategoryUseCase {

    /**
     * Retrieves the list of all item categories.
     *
     * @return list of ItemCategoryDTO
     */
    List<ItemCategoryDTO> list();

    /**
     * Inserts a new item category.
     *
     * @param dto the item category data to insert
     */
    void insert(ItemCategoryDTO dto);

    /**
     * Updates an existing item category.
     *
     * @param dto the item category data to update
     */
    void update(ItemCategoryDTO dto);

    /**
     * Deletes an existing item category.
     *
     * @param dto the item category data to delete
     */
    void delete(ItemCategoryDTO dto);
}
