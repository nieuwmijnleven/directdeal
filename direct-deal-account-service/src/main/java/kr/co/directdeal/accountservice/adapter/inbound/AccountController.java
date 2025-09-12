package kr.co.directdeal.accountservice.adapter.inbound;

import jakarta.validation.Valid;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import kr.co.directdeal.accountservice.port.inbound.AccountUseCase;
import kr.co.directdeal.accountservice.application.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.application.service.dto.PasswordDTO;
import kr.co.directdeal.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that handles account-related operations such as
 * account creation, update, deletion, and password changes.
 *
 * <p>This controller interacts with the AccountUseCase to process business logic.</p>
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountUseCase accountUseCase;

    /**
     * Returns the list of HTTP methods supported by this endpoint.
     *
     * @return allowed HTTP methods
     */
    @Operation(summary = "Check supported HTTP methods", description = "Returns the list of HTTP methods supported by this endpoint.")
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.DELETE, HttpMethod.OPTIONS)
                .build();
    }

    /**
     * Creates a new user account.
     *
     * @param accountDTO the account details to be created
     */
    @Operation(summary = "Create account", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(
            @Parameter(description = "Account data", required = true)
            @Valid @RequestBody AccountDTO accountDTO) {
        accountUseCase.createAccount(accountDTO);
    }

    /**
     * Updates the current user's account information.
     *
     * @param accountDTO the updated account details
     */
    @Operation(summary = "Update account", description = "Updates the account information of the currently logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateAccount(
            @Parameter(description = "Updated account data", required = true)
            @Valid @RequestBody AccountDTO accountDTO) {
        accountDTO.setEmail(SecurityUtils.getCurrentUserLogin());
        accountUseCase.updateAccount(accountDTO);
    }

    /**
     * Retrieves the currently logged-in user's account information.
     *
     * @return the account details
     */
    @Operation(summary = "Get account", description = "Retrieves the account information of the currently logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping
    public AccountDTO getAccount() {
        String loginEmail = SecurityUtils.getCurrentUserLogin();
        return accountUseCase.getAccount(loginEmail);
    }

    /**
     * Deletes the currently logged-in user's account.
     */
    @Operation(summary = "Delete account", description = "Deletes the currently logged-in user's account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount() {
        String loginEmail = SecurityUtils.getCurrentUserLogin();
        accountUseCase.deleteAccount(loginEmail);
    }

    /**
     * Changes the password of the currently logged-in user.
     *
     * @param passwordDTO the password change details
     */
    @Operation(summary = "Change password", description = "Changes the password of the currently logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully changed"),
            @ApiResponse(responseCode = "400", description = "Invalid password data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @Parameter(description = "Password change data", required = true)
            @Valid @RequestBody PasswordDTO passwordDTO) {
        String loginEmail = SecurityUtils.getCurrentUserLogin();
        log.debug("loginEmail => " + loginEmail);
        accountUseCase.changePassword(loginEmail, passwordDTO);
    }
}
