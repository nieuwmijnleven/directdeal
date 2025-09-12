package kr.co.directdeal.chattingservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Custom exception class for handling chatting-related exceptions.
 *
 * Supports internationalized error messages using a message key and optional arguments.
 */
@Getter
@NoArgsConstructor
public class ChattingException extends RuntimeException {

    /**
     * The message key for internationalization (i18n) to look up the error message.
     */
    private String messageKey;

    /**
     * Arguments to be substituted in the error message.
     */
    private Object[] messageArgs;

    /**
     * Constructs a new ChattingException with a message key and optional arguments.
     *
     * @param messageKey the key to lookup the error message
     * @param messageArgs optional arguments for the message
     */
    @Builder
    public ChattingException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructs a new ChattingException with the specified message.
     *
     * @param message the detail message
     */
    public ChattingException(String message) {
        super(message);
    }
}
