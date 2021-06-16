package kr.co.directdeal.saleservice.service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

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
    
    @Size(min = 5, max = 64)
    private String ownerId;
    
    @NotNull
    @Size(min = 1, max = 128)
    private String title;
    
    @NotNull
    @Size(min = 1, max = 128)
    private String category;
    
    @NotNull
    @Positive
    private long targetPrice;

    private boolean discountable;
    
    @NotNull
    @Size(min = 1, max = 1024)
    private String text;
    
    private List<String> images = new ArrayList<>();
    
    private SaleItemStatus status;
    
    // private String createdBy;
    
    private Instant createdDate; 
    
    // private String lastModifiedBy;
    
    private Instant lastModifiedByDate;
}
