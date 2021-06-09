package kr.co.directdeal.chattingservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public ChattingException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public ChattingException(String message) {
        super(message);
    }   
}