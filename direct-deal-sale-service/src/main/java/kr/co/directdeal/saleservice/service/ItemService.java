package kr.co.directdeal.saleservice.service;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.directdeal.common.sale.command.ItemDeleteCommand;
import kr.co.directdeal.common.sale.command.ItemRegisterCommand;
import kr.co.directdeal.common.sale.command.ItemSaleCompleteCommand;
import kr.co.directdeal.common.sale.command.ItemSaleStartCommand;
import kr.co.directdeal.common.sale.command.ItemSaleStopCommand;
import kr.co.directdeal.common.sale.command.ItemUpdateCommand;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.saleservice.service.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {

    private final CommandGateway commandGateway;

    public void register(ItemDTO itemDTO) {
        log.debug("calll {}.register(), id = {}", this.getClass().getSimpleName(), itemDTO.getId());
        
        commandGateway.send(ItemRegisterCommand.builder()
                                .id(UUID.randomUUID().toString())
                                .title(itemDTO.getTitle())
                                .category(itemDTO.getCategory())
                                .targetPrice(itemDTO.getTargetPrice())
                                .text(itemDTO.getText())
                                .images(itemDTO.getImages())
                                .status(SaleItemStatus.SALE)
                                .build());
    }

    public void update(ItemDTO itemDTO) {
        log.debug("calll {}.update(), id = {}", this.getClass().getSimpleName(), itemDTO.getId());
        commandGateway.send(ItemUpdateCommand.builder()
                                .id(itemDTO.getId())
                                .title(itemDTO.getTitle())
                                .category(itemDTO.getCategory())
                                .targetPrice(itemDTO.getTargetPrice())
                                .text(itemDTO.getText())
                                .images(itemDTO.getImages())
                                .status(itemDTO.getStatus())
                                .build());
    }

    public void delete(@PathVariable("id") String id) {
        log.debug("calll {}.delete({})", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemDeleteCommand.builder()
                                .id(id)
                                .build());
    }

    public void sale(String id) {
        log.debug("calll {}.sale(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleStartCommand.builder()
                                .id(id)
                                .build());
    }

    public void pause(String id) {
        log.debug("calll {}.pause(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleStopCommand.builder()
                                .id(id)
                                .build());
    }

    public void complete(String id) {
        log.debug("calll {}.complete(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleCompleteCommand.builder()
                                .id(id)
                                .build());
    }
}
