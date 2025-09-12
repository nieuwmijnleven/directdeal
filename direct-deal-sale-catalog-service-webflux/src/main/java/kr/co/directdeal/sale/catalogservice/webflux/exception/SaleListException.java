package kr.co.directdeal.sale.catalogservice.webflux.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Exception thrown for errors related to SaleList operations.
 * <p>
 * Contains a message key and optional message arguments for internationalization support.
 * </p>
 *
 * @author Cheol Jeon
 */
@Getter
@NoArgsConstructor
public class SaleListException extends RuntimeException {

    /**
     * The message key for localization or error message resolution.
     */
    private String messageKey;

    /**
     * Arguments to be used with the message key for detailed error information.
     */
    private Object[] messageArgs;

    /**
     * Constructs a SaleListException with a message key and message arguments.
     *
     * @param messageKey the key identifying the error message
     * @param messageArgs arguments to be used in the error message
     */
    @Builder
    public SaleListException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructs a SaleListException with a plain message.
     *
     * @param message the error message
     */
    public SaleListException(String message) {
        super(message);
    }
}
