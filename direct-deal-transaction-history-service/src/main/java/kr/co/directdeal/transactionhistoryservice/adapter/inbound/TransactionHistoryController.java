package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.application.service.TransactionHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing transaction history.
 *
 * <p>Provides endpoints to view and update transaction records.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction-history")
@Tag(name = "Transaction History", description = "Endpoints for managing transaction history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    /**
     * Retrieves a list of transaction history records for the current user.
     *
     * @return list of transaction records
     */
    @Operation(summary = "Get transaction history list", description = "Retrieve a list of transaction records for the logged-in user.")
    @GetMapping
    public List<TransactionHistoryDTO> list() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return transactionHistoryService.list(userId);
    }

    /**
     * Assigns a buyer to an existing transaction.
     *
     * @param dto the transaction history data including buyer information
     */
    @Operation(summary = "Set buyer for transaction", description = "Assign a buyer to a specific transaction.")
    @PutMapping("/setbuyer")
    public void setBuyer(@RequestBody TransactionHistoryDTO dto) {
        log.debug("TransactionHistoryController.setBuyer(), dto => " + dto);
        transactionHistoryService.setBuyer(dto);
    }
}
