package kr.co.directdeal.saleservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemImageException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public ItemImageException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public ItemImageException(String message) {
        super(message);
    }   
}