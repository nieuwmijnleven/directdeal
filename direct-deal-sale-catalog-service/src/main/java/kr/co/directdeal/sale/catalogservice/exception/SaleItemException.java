package kr.co.directdeal.sale.catalogservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaleItemException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public SaleItemException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public SaleItemException(String message) {
        super(message);
    }   
}