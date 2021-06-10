package kr.co.directdeal.sale.catalogservice.service.dto;

import java.time.Instant;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
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
public class SaleListItemDTO {
    private String id;
    private String title;
    private String category;
    private long targetPrice;
    private String mainImage;
    private SaleItemStatus status;
    private Instant createdDate; 
}
