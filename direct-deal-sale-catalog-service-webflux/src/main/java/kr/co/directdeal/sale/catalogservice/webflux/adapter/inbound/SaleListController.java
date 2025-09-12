package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound.response.LiftUpResponse;
import kr.co.directdeal.sale.catalogservice.webflux.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.SaleListService;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing sale item listings.
 * <p>
 * Provides endpoints to retrieve sale lists and perform lift-up operations.
 * </p>
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/salelist")
@Tag(name = "SaleList Controller", description = "Endpoints for listing and managing sale items in list view")
public class SaleListController {

    private final SaleListService saleListService;

    private final LiftUpProperties liftUpProperties;

    /**
     * Retrieves a paginated list of sale items.
     *
     * @param pageable pagination information
     * @return a Flux stream of SaleListDTO
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get paginated list of sale items",
            description = "Retrieves a paginated list of all sale items available in the catalog."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sale items retrieved successfully")
    })
    public Flux<SaleListDTO> list(Pageable pageable) {
        log.debug("pageable => {}", pageable);
        return saleListService.list(pageable);
    }

    /**
     * Performs a lift-up operation on a sale item, pushing it to the top of the list.
     * <p>
     * The lift-up can only be done after a certain number of days has passed, as configured.
     * </p>
     *
     * @param id the ID of the sale item to lift up
     * @return a ResponseEntity containing the lift-up result and interval days
     */
    @PutMapping("/{id}/lift-up")
    @Operation(
            summary = "Lift up a sale item",
            description = "Promotes a sale item to appear at the top of the list, if the lift-up interval has passed."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lift-up operation completed successfully"),
            @ApiResponse(responseCode = "400", description = "Lift-up not allowed yet due to interval restriction"),
            @ApiResponse(responseCode = "404", description = "Sale item not found")
    })
    public ResponseEntity<LiftUpResponse> liftUp(@PathVariable("id") @NotBlank String id) {
        boolean liftUpResult = saleListService.liftUp(id);
        return ResponseEntity.ok(
                LiftUpResponse.builder()
                        .result(liftUpResult ? LiftUpResponse.ResultConstants.SUCCESS : LiftUpResponse.ResultConstants.FAILURE)
                        .intervalDays(liftUpProperties.getLiftUpIntervalDays())
                        .build()
        );
    }
}
