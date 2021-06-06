package kr.co.directdeal.common.sale.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class ItemSaleCompleteCommand {
    @TargetAggregateIdentifier
    private String id;   
}