package kr.co.directdeal.saleservice.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleItemDTO {
    private String id;
    private String title;
    private String category;
    private long targetPrice;
    private String text;
    private String imagePath;
    private String status;
    private String createdBy;
    private Instant createdDate; 
    private String lastModifiedBy;
    private Instant lastModifiedByDate;
}
