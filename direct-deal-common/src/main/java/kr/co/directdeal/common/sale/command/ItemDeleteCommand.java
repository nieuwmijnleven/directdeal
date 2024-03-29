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
public class ItemDeleteCommand {
    @TargetAggregateIdentifier
    private String id;    
}
