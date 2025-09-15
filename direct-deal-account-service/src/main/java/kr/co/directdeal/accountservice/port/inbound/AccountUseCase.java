package kr.co.directdeal.accountservice.port.inbound;

import kr.co.directdeal.accountservice.application.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.application.service.dto.PasswordDTO;

/**
 * Interface defining account-related use cases.
 *
 * <p>This interface provides methods for creating, updating,
 * retrieving, deleting accounts, and changing passwords.</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
public interface AccountUseCase {

    /**
     * Creates a new account.
     *
     * @param accountDTO the account data transfer object containing account info
     * @return the created account as AccountDTO
     */
    AccountDTO createAccount(AccountDTO accountDTO);

    /**
     * Updates an existing account based on email.
     *
     * @param accountDTO the account data transfer object containing updated info
     * @return the updated account as AccountDTO
     */
    AccountDTO updateAccount(AccountDTO accountDTO);

    /**
     * Updates an existing account based on account ID.
     *
     * @param accountDTO the account data transfer object containing updated info
     * @return the updated account as AccountDTO
     */
    AccountDTO updateAccountById(AccountDTO accountDTO);

    /**
     * Retrieves an account by email.
     *
     * @param email the email of the account to retrieve
     * @return the retrieved account as AccountDTO
     */
    AccountDTO getAccount(String email);

    /**
     * Deletes an account by email.
     *
     * @param email the email of the account to delete
     */
    void deleteAccount(String email);

    /**
     * Changes the password of the account identified by email.
     *
     * @param email the email of the account
     * @param passwordDTO the password data transfer object containing old and new passwords
     */
    void changePassword(String email, PasswordDTO passwordDTO);

}
