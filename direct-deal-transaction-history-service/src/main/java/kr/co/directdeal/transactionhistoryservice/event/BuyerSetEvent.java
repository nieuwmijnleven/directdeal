package kr.co.directdeal.transactionhistoryservice.event;

import org.springframework.context.ApplicationEvent;

import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;
import lombok.ToString;

@ToString
public class BuyerSetEvent extends ApplicationEvent {

    private TransactionHistoryDTO transactionHistoryDTO;
    
    public BuyerSetEvent(TransactionHistoryDTO source) {
        super(source);
        this.transactionHistoryDTO = source;
    }

    public TransactionHistoryDTO getTransactionHistoryDTO() {
        return this.transactionHistoryDTO;
    } 
}
