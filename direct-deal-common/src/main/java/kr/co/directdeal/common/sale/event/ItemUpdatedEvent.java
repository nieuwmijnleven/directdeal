package kr.co.directdeal.common.sale.event;

import java.util.List;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemUpdatedEvent {
    @TargetAggregateIdentifier
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
