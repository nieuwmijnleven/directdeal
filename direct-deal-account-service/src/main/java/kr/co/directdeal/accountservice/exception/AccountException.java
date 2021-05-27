package kr.co.directdeal.accountservice.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public AccountException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public AccountException(String message) {
        super(message);
    }   
}