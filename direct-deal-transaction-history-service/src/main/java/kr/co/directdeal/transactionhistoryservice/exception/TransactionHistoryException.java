package kr.co.directdeal.transactionhistoryservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom exception for Transaction History related errors.
 * Contains message key and arguments for internationalization support.
 *
 * @author Cheol Jeon
 */
@Getter
@NoArgsConstructor
public class TransactionHistoryException extends RuntimeException {

    private String messageKey;

    private Object[] messageArgs;

    /**
     * Constructs a TransactionHistoryException with message key and arguments.
     *
     * @param messageKey the key for the message to be localized
     * @param messageArgs the arguments for the message
     */
    @Builder
    public TransactionHistoryException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructs a TransactionHistoryException with a simple message.
     *
     * @param message the error message
     */
    public TransactionHistoryException(String message) {
        super(message);
    }
}
