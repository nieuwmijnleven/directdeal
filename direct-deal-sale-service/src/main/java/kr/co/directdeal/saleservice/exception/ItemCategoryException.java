package kr.co.directdeal.saleservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Exception class representing errors related to ItemCategory operations.
 *
 * Supports message keys and arguments for internationalization or detailed error messages.
 *
 * @author Cheol Jeon
 */
@Getter
@NoArgsConstructor
public class ItemCategoryException extends RuntimeException {

    /**
     * Message key for i18n or specific error identification.
     */
    private String messageKey;

    /**
     * Arguments related to the message key for formatting.
     */
    private Object[] messageArgs;

    /**
     * Builder constructor for creating an exception with message key and arguments.
     *
     * @param messageKey the key representing the error message
     * @param messageArgs arguments for the error message
     */
    @Builder
    public ItemCategoryException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructor for creating an exception with a simple message.
     *
     * @param message the detail message
     */
    public ItemCategoryException(String message) {
        super(message);
    }
}
