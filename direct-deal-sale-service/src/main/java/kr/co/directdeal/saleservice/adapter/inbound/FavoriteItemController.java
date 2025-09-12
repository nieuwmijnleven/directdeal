package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.application.service.FavoriteItemService;
import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing user's favorite items.
 * Provides endpoints to list, save, and delete favorite items.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/favorite")
@Tag(name = "Favorite Item API", description = "API for managing favorite items")
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    /**
     * Retrieves the list of favorite items for the current user.
     *
     * @return List of FavoriteItemDTO objects
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List Favorite Items", description = "Get all favorite items for the current user")
    public List<FavoriteItemDTO> list() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return favoriteItemService.list(FavoriteItemDTO.builder()
                .userId(userId)
                .build());
    }

    /**
     * Saves a new favorite item for the current user.
     *
     * @param dto FavoriteItemDTO object to save
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save Favorite Item", description = "Add a new favorite item for the current user")
    public void save(@RequestBody FavoriteItemDTO dto) {
        String userId = SecurityUtils.getCurrentUserLogin();
        dto.setUserId(userId);
        log.debug("save => " + dto);
        favoriteItemService.save(dto);
    }

    /**
     * Deletes a favorite item of the current user by itemId.
     *
     * @param itemId ID of the item to delete from favorites
     */
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Favorite Item", description = "Remove a favorite item by itemId for the current user")
    public void delete(@PathVariable("itemId") String itemId) {
        String userId = SecurityUtils.getCurrentUserLogin();
        favoriteItemService.delete(FavoriteItemDTO.builder()
                .userId(userId)
                .itemId(itemId)
                .build());
    }
}
