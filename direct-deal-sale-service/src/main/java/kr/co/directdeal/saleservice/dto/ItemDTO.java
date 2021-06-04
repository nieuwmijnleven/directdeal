package kr.co.directdeal.saleservice.dto;

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
public class ItemDTO {
    private String id;

    private String ownerId;
    
    private String title;
    
    private String category;
    
    private long targetPrice;

    private boolean discountable;
    
    private String text;
    
    private List<String> images;
    
    private SaleItemStatus status;
    
    // private String createdBy;
    
    // private Instant createdDate; 
    
    // private String lastModifiedBy;
    
    // private Instant lastModifiedByDate;
}
