package kr.co.directdeal.accountservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom exception class for handling account-related errors.
 *
 * <p>This exception supports internationalized error messages using
 * message keys and arguments.</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
@Getter
@NoArgsConstructor
public class AccountException extends RuntimeException {

    /**
     * The message key for internationalized error messages.
     */
    private String messageKey;

    /**
     * The arguments to be used with the message key.
     */
    private Object[] messageArgs;

    /**
     * Builder constructor for AccountException with message key and arguments.
     *
     * @param messageKey the message key to identify the error message
     * @param messageArgs the arguments for the message key
     */
    @Builder
    public AccountException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructor for AccountException with a plain error message.
     *
     * @param message the error message
     */
    public AccountException(String message) {
        super(message);
    }
}
