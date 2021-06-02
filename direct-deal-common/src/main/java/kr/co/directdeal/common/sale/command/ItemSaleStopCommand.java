package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ItemSaleStopCommand {
    @TargetAggregateIdentifier
    private String id;    
    private String status;
}
