package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.directdeal.saleservice.application.service.ItemCategoryService;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing item categories.
 * Provides endpoints to list, insert, update, and delete categories.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "Item Category API", description = "API for managing item categories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    /**
     * Get the list of all item categories.
     *
     * @return list of ItemCategoryDTO
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List Item Categories", description = "Get all item categories")
    public List<ItemCategoryDTO> list() {
        return itemCategoryService.list();
    }

    /**
     * Insert a new item category.
     *
     * @param dto ItemCategoryDTO to insert
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Insert Item Category", description = "Insert a new item category")
    public void insert(@RequestBody ItemCategoryDTO dto) {
        log.debug("save => " + dto);
        itemCategoryService.insert(dto);
    }

    /**
     * Update an existing item category.
     *
     * @param dto ItemCategoryDTO to update
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update Item Category", description = "Update an existing item category")
    public void update(@RequestBody ItemCategoryDTO dto) {
        log.debug("update => " + dto);
        itemCategoryService.update(dto);
    }

    /**
     * Delete an item category by ID.
     *
     * @param id category ID to delete
     */
    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Item Category", description = "Delete an item category by ID")
    public void delete(@PathVariable("id") Long id) {
        itemCategoryService.delete(ItemCategoryDTO.builder()
                .id(id)
                .build());
    }
}
