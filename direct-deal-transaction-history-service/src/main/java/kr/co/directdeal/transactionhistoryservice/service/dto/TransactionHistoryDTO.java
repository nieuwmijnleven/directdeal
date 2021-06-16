package kr.co.directdeal.transactionhistoryservice.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TransactionHistoryDTO {
    private Long id;
    private String itemId;
    private String sellerId;
    private String title;
    private String category;
    private long targetPrice;
    private String buyerId;
    private Instant completionTime; 
}
