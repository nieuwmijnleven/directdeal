package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.adapter.inbound.result.ItemRegistrationResult;
import kr.co.directdeal.saleservice.application.service.ItemService;
import kr.co.directdeal.saleservice.application.service.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for managing items.
 * Provides endpoints for registering, updating, deleting, and changing item status.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
@Tag(name = "Item API", description = "API for managing items")
public class ItemController {

    private final ItemService itemService;

    /**
     * Register a new item.
     * Generates a UUID for the item and assigns current user as owner.
     *
     * @param itemDTO Item data transfer object with item details
     * @return ItemRegistrationResult containing the generated item ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register Item", description = "Register a new item with generated ID and current user as owner")
    public ItemRegistrationResult register(@Valid @RequestBody ItemDTO itemDTO) {
        String itemId = UUID.randomUUID().toString();
        String userId = SecurityUtils.getCurrentUserLogin();

        itemDTO.setId(itemId);
        itemDTO.setOwnerId(userId);
        itemService.register(itemDTO);

        return ItemRegistrationResult.builder()
                .itemId(itemId)
                .build();
    }

    /**
     * Update an existing item.
     *
     * @param itemDTO Item data transfer object with updated item details
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update Item", description = "Update an existing item")
    public void update(@Valid @RequestBody ItemDTO itemDTO) {
        itemService.update(itemDTO);
    }

    /**
     * Delete an item by ID.
     *
     * @param id Item ID to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Item", description = "Delete an item by its ID")
    public void delete(@PathVariable("id") @NotBlank String id) {
        itemService.delete(id);
    }

    /**
     * Change the item status to 'sale'.
     *
     * @param id Item ID to mark as sale
     */
    @PutMapping("/{id}/sale")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Mark Item as Sale", description = "Mark the item status as 'sale'")
    public void sale(@PathVariable("id") @NotBlank String id) {
        itemService.sale(id);
    }

    /**
     * Change the item status to 'pause'.
     *
     * @param id Item ID to pause
     */
    @PutMapping("/{id}/pause")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Pause Item", description = "Pause the item status")
    public void pause(@PathVariable("id") @NotBlank String id) {
        itemService.pause(id);
    }

    /**
     * Change the item status to 'complete'.
     *
     * @param id Item ID to complete
     */
    @PutMapping("/{id}/complete")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Complete Item", description = "Complete the item status")
    public void complete(@PathVariable("id") @NotBlank String id) {
        itemService.complete(id);
    }
}
