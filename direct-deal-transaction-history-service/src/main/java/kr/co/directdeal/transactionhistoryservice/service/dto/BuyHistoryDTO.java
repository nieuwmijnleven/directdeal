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
public class BuyHistoryDTO {
    private Long id;
    private String buyerId;
    private String itemId;
    private String title;
    private String category;
    private long targetPrice;
    private String mainImage;
    private String sellerId;
    private Instant completionTime; 
}
