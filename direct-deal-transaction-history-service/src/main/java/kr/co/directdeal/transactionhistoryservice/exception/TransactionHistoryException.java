package kr.co.directdeal.transactionhistoryservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransactionHistoryException extends RuntimeException {

    private String messageKey;
    
    private Object[] messageArgs;

    @Builder
    public TransactionHistoryException(String messageKey, Object[] messageArgs) {
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public TransactionHistoryException(String message) {
        super(message);
    }   
}