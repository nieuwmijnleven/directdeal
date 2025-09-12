package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.sale.catalogservice.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for handling sale item related operations.
 * <p>
 * Provides endpoints to retrieve a single sale item or a list of items owned by the current seller.
 * </p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/saleitem")
@Tag(name = "SaleItem Controller", description = "Endpoints for accessing and managing sale items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    /**
     * Retrieves detailed information of a specific sale item by its ID.
     *
     * @param id The unique identifier of the sale item.
     * @return SaleItemDTO object containing detailed sale item information.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get sale item by ID",
            description = "Retrieves detailed information about a sale item using its ID."
    )
    public SaleItemDTO getSaleItem(@PathVariable("id") @NotBlank String id) {
        return saleItemService.findSaleItemById(id);
    }

    /**
     * Retrieves all sale items owned by the currently authenticated user (seller).
     *
     * @return A list of SaleItemDTOs owned by the current user.
     */
    @GetMapping("/seller-items")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get seller's sale items",
            description = "Retrieves all sale items created by the currently logged-in seller."
    )
    public List<SaleItemDTO> getSellerSaleItems() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return saleItemService.findSaleItemsByOwnerId(userId);
    }
}
