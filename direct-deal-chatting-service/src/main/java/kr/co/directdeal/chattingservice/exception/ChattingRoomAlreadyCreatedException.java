package kr.co.directdeal.chattingservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingRoomAlreadyCreatedException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public ChattingRoomAlreadyCreatedException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public ChattingRoomAlreadyCreatedException(String message) {
        super(message);
    }   
}