package kr.co.directdeal.saleservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemCategoryException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public ItemCategoryException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public ItemCategoryException(String message) {
        super(message);
    }   
}