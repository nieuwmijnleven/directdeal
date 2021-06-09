package kr.co.directdeal.transactionhistoryservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuyHistoryException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public BuyHistoryException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public BuyHistoryException(String message) {
        super(message);
    }   
}