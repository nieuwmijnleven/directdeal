package kr.co.directdeal.transactionhistoryservice.event;

import org.springframework.context.ApplicationEvent;

import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import lombok.ToString;

/**
 * Event published when a buyer is set for a transaction.
 * Contains the TransactionHistoryDTO related to the event.
 *
 * @author Cheol Jeon
 */
@ToString
public class BuyerSetEvent extends ApplicationEvent {

    private TransactionHistoryDTO transactionHistoryDTO;

    /**
     * Constructor to create a BuyerSetEvent.
     *
     * @param source the TransactionHistoryDTO source of the event
     */
    public BuyerSetEvent(TransactionHistoryDTO source) {
        super(source);
        this.transactionHistoryDTO = source;
    }

    /**
     * Returns the TransactionHistoryDTO associated with this event.
     *
     * @return transactionHistoryDTO
     */
    public TransactionHistoryDTO getTransactionHistoryDTO() {
        return this.transactionHistoryDTO;
    }
}
