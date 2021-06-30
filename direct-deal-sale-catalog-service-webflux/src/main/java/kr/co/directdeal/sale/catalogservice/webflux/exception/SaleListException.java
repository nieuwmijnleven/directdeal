package kr.co.directdeal.sale.catalogservice.webflux.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaleListException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public SaleListException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public SaleListException(String message) {
        super(message);
    }   
}