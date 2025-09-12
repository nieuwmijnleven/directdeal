package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.ItemDTO;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Use case interface for managing sale items.
 *
 * Defines operations such as registration, updating, deletion, and sale status transitions
 * for items that are listed for sale.
 *
 * @author Cheol Jeon
 */
public interface ItemUseCase {

    /**
     * Registers a new sale item.
     *
     * @param itemDTO the item data to register
     */
    void register(ItemDTO itemDTO);

    /**
     * Updates an existing sale item.
     *
     * @param itemDTO the updated item data
     */
    void update(ItemDTO itemDTO);

    /**
     * Deletes a sale item by ID.
     *
     * @param id the identifier of the item to delete
     */
    void delete(@PathVariable("id") String id);

    /**
     * Starts the sale of an item.
     *
     * @param id the identifier of the item to start sale
     */
    void sale(String id);

    /**
     * Pauses the sale of an item.
     *
     * @param id the identifier of the item to pause sale
     */
    void pause(String id);

    /**
     * Marks an item as sale completed.
     *
     * @param id the identifier of the item to complete sale
     */
    void complete(String id);
}
