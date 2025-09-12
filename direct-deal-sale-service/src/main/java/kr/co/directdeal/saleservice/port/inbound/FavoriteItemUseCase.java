package kr.co.directdeal.saleservice.port.inbound;

import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use case interface for managing Favorite Items.
 *
 * Provides methods to list, save, and delete favorite items.
 *
 * @author Cheol Jeon
 */
public interface FavoriteItemUseCase {

    /**
     * Retrieves a list of favorite items based on the criteria in the given DTO.
     *
     * @param dto the data transfer object containing search criteria
     * @return list of matching FavoriteItemDTOs
     */
    List<FavoriteItemDTO> list(FavoriteItemDTO dto);

    /**
     * Saves a favorite item.
     *
     * @param dto the favorite item data to save
     */
    void save(FavoriteItemDTO dto);

    /**
     * Deletes a favorite item.
     *
     * @param dto the favorite item data to delete
     */
    void delete(FavoriteItemDTO dto);
}
