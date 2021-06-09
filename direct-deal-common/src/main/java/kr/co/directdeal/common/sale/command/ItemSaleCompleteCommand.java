package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ItemSaleCompleteCommand {
    @TargetAggregateIdentifier
    private String id;   
    private String ownerId;
    private String title;
    private String category;
    private long targetPrice; 
}
