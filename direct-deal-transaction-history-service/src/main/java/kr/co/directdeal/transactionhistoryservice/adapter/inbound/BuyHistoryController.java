package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.application.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller that handles REST API operations related to buyer transaction history.
 * It provides endpoints for retrieving and deleting purchase records for the authenticated user.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/buy-history")
@Tag(name = "Buy History", description = "API for managing buyer's transaction history")
public class BuyHistoryController {

    private final BuyHistoryService buyHistoryService;

    /**
     * Returns a list of buy history records for the current authenticated user.
     *
     * @return List of {@link BuyHistoryDTO}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get buyer history list", description = "Returns the list of items purchased by the current user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of buy history.")
    public List<BuyHistoryDTO> list() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return buyHistoryService.list(userId);
    }

    /**
     * Deletes a specific buy history record for the authenticated user.
     *
     * @param id The ID of the buy history record to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete buy history", description = "Deletes a specific buy history entry by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the buy history entry."),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner of the entry."),
            @ApiResponse(responseCode = "404", description = "Buy history entry not found.")
    })
    public void delete(@PathVariable("id") Long id) {
        String buyerId = SecurityUtils.getCurrentUserLogin();
        buyHistoryService.delete(BuyHistoryDTO.builder()
                .id(id)
                .buyerId(buyerId)
                .build());
    }
}
