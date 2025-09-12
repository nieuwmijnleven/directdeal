package kr.co.directdeal.chattingservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Exception thrown when attempting to create a chatting room
 * that already exists for the given item, seller, and customer.
 *
 * Supports internationalized error messages using a message key and optional arguments.
 */
@Getter
@NoArgsConstructor
public class ChattingRoomAlreadyCreatedException extends RuntimeException {

    /**
     * The message key used for internationalization (i18n) of the error message.
     */
    private String messageKey;

    /**
     * Arguments for the error message template.
     */
    private Object[] messageArgs;

    /**
     * Constructs a new ChattingRoomAlreadyCreatedException with the specified message key and arguments.
     *
     * @param messageKey the key to lookup the error message
     * @param messageArgs optional arguments for the error message
     */
    @Builder
    public ChattingRoomAlreadyCreatedException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    /**
     * Constructs a new ChattingRoomAlreadyCreatedException with the specified detail message.
     *
     * @param message the detail message
     */
    public ChattingRoomAlreadyCreatedException(String message) {
        super(message);
    }
}
