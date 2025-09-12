package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * WebFlux REST controller for handling sale item-related endpoints.
 * <p>
 * Provides endpoints to retrieve individual sale items or
 * all sale items owned by the currently authenticated seller.
 * </p>
 *
 * This controller uses reactive types (Mono, Flux) to support non-blocking operations.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/saleitem")
@Tag(name = "SaleItem Controller", description = "Endpoints for retrieving sale items")
public class SaleItemController {

    private final SaleItemService saleItemService;

    /**
     * Retrieves a sale item by its ID.
     *
     * @param id the ID of the sale item
     * @return a Mono emitting the sale item DTO if found
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get sale item by ID",
            description = "Returns a single sale item that matches the given ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale item retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated"),
            @ApiResponse(responseCode = "404", description = "Sale item not found")
    })
    public Mono<SaleItemDTO> getSaleItem(@PathVariable("id") @NotBlank String id) {
        return saleItemService.findSaleItemById(id);
    }

    /**
     * Retrieves all sale items that belong to the currently authenticated user.
     *
     * @return a Flux emitting the list of the user's sale items
     */
    @GetMapping("/seller-items")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get sale items of the current seller",
            description = "Returns a list of sale items created by the currently logged-in user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller's sale items retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user must be logged in")
    })
    public Flux<SaleItemDTO> getSellerSaleItems() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMapMany(saleItemService::findSaleItemsByOnwerId);
    }
}
