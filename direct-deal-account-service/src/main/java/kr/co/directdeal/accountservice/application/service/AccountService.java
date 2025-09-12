package kr.co.directdeal.accountservice.application.service;

import java.util.Collections;
import java.util.Objects;

import kr.co.directdeal.accountservice.port.inbound.AccountUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.accountservice.domain.object.Account;
import kr.co.directdeal.accountservice.domain.object.Authority;
import kr.co.directdeal.accountservice.exception.AccountException;
import kr.co.directdeal.accountservice.application.service.dto.AccountDTO;
import kr.co.directdeal.accountservice.application.service.dto.PasswordDTO;
import kr.co.directdeal.accountservice.application.service.mapper.Mapper;
import kr.co.directdeal.accountservice.port.outbound.AccountRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing accounts.
 *
 * <p>This service provides functionality to create, update, retrieve, delete accounts
 * and change passwords, implementing {@link AccountUseCase} interface.</p>
 *
 * <p>All methods are transactional to ensure data integrity.</p>
 *
 * <p>Custom exceptions are thrown for error conditions, using {@link AccountException}.</p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Service
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;

    private final Mapper<Account, AccountDTO> mapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new account with the provided account data.
     * Validates if the email is not already in use.
     *
     * @param accountDTO the account data transfer object containing information to create an account
     * @return the created account as AccountDTO
     * @throws AccountException if the email is already used
     */
    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        boolean unusedEmail = accountRepository
                .findByEmail(accountDTO.getEmail())
                .map(accountByEmail -> Boolean.FALSE)
                .orElse(Boolean.TRUE);
        if (!unusedEmail)
            throw AccountException.builder()
                    .messageKey("account.exception.insert.email.message")
                    .messageArgs(new String[]{accountDTO.getEmail()})
                    .build();

        Account newAccount = Account.builder()
                .id(accountDTO.getId())
                .email(accountDTO.getEmail())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .name(accountDTO.getName())
                .activated(true)
                .authorities(Collections.singleton(Authority.USER))
                .build();

        return mapper.toDTO(accountRepository.save(newAccount));
    }

    /**
     * Updates an existing account identified by email.
     *
     * @param accountDTO the account data transfer object with updated information
     * @return the updated account as AccountDTO
     * @throws AccountException if account with given email is not found
     */
    @Override
    @Transactional
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        Account accountByEmail = accountRepository
                .findByEmail(accountDTO.getEmail())
                .orElseThrow(() -> AccountException.builder()
                        .messageKey("account.exception.update.id.message")
                        .messageArgs(new String[]{accountDTO.getEmail()})
                        .build());

        accountByEmail.updateFrom(accountDTO);
        return mapper.toDTO(accountByEmail);
    }

    /**
     * Updates an existing account identified by account ID.
     * Also checks that the new email is either unused or belongs to the same account.
     *
     * @param accountDTO the account data transfer object with updated information
     * @return the updated account as AccountDTO
     * @throws AccountException if account with given ID is not found or if email is already used by another account
     */
    public AccountDTO updateAccountById(AccountDTO accountDTO) {
        Account accountById = accountRepository
                .findById(accountDTO.getId())
                .orElseThrow(() -> AccountException.builder()
                        .messageKey("account.exception.update.id.message")
                        .messageArgs(new String[]{accountDTO.getId().toString()})
                        .build());

        boolean validEmail = accountRepository
                .findByEmail(accountDTO.getEmail())
                .filter(accountByEmail -> !Objects.equals(accountById.getId(), accountByEmail.getId()))
                .map(accountByEmail -> Boolean.FALSE)
                .orElse(Boolean.TRUE);
        if (!validEmail)
            throw AccountException.builder()
                    .messageKey("account.exception.update.email.message")
                    .messageArgs(new String[]{accountDTO.getEmail()})
                    .build();

        accountById.updateFrom(accountDTO);
        return mapper.toDTO(accountById);
    }

    /**
     * Retrieves an account by the email.
     *
     * @param email the email of the account to retrieve
     * @return the account data as AccountDTO
     * @throws AccountException if no account found with the given email
     */
    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccount(String email) {
        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() -> AccountException.builder()
                        .messageKey("account.exception.get.message")
                        .messageArgs(new String[]{email})
                        .build());

        return mapper.toDTO(account);
    }

    /**
     * Deletes an account identified by the email.
     *
     * @param email the email of the account to delete
     * @throws AccountException if no account found with the given email
     */
    @Override
    @Transactional
    public void deleteAccount(String email) {
        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() -> AccountException.builder()
                        .messageKey("account.exception.delete.message")
                        .messageArgs(new String[]{email})
                        .build());

        accountRepository.delete(account);
    }

    /**
     * Changes the password for the account identified by email.
     * Validates that the new password is different from the old password and matches the current password.
     *
     * @param email the email of the account for which to change the password
     * @param passwordDTO the DTO containing current and new password
     * @throws AccountException if the new password is the same as the old password,
     *                          if the account is not found, or
     *                          if the current password does not match
     */
    @Override
    @Transactional
    public void changePassword(String email, PasswordDTO passwordDTO) {
        if (passwordDTO.isSamePasswords()) {
            throw AccountException.builder()
                    .messageKey("account.exception.changepassword.samepasswords.message")
                    .build();
        }

        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() -> AccountException.builder()
                        .messageKey("account.exception.changepassword.accountnotfound.message")
                        .messageArgs(new String[]{email})
                        .build());

        String encodedPassword = account.getPassword();
        if (!passwordEncoder.matches(passwordDTO.getPassword(), encodedPassword)) {
            throw AccountException.builder()
                    .messageKey("account.exception.changepassword.passwordmismatch.message")
                    .build();
        }

        String encodedNewPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        account.changePassword(encodedNewPassword);
    }
}
