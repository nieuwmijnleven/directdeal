package kr.co.directdeal.saleservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteItemException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public FavoriteItemException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public FavoriteItemException(String message) {
        super(message);
    }   
}