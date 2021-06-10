package kr.co.directdeal.sale.catalogservice.service.dto;

import java.time.Instant;
import java.util.List;

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
public class SaleItemDTO {
    private String id;
    private String title;
    private String category;
    private long targetPrice;
    private String text;
    private List<String> images;
    private SaleItemStatus status;
    private String createdBy;
    private Instant createdDate; 
    private String lastModifiedBy;
    private Instant lastModifiedByDate;
}
