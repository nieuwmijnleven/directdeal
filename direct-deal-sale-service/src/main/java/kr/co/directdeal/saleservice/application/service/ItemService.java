package kr.co.directdeal.saleservice.application.service;

import java.time.Instant;

import kr.co.directdeal.saleservice.port.inbound.ItemUseCase;
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
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.application.service.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation of ItemUseCase interface.
 * Handles item-related commands such as registering, updating, deleting, and managing sales state.
 * Uses Axon CommandGateway to send commands asynchronously.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService implements ItemUseCase {

    private final CommandGateway commandGateway;

    /**
     * Registers a new item by sending ItemRegisterCommand through CommandGateway.
     * The current logged-in user is set as the owner.
     * Status is set to SALE by default.
     *
     * @param itemDTO the item data transfer object containing item details
     */
    @Override
    public void register(ItemDTO itemDTO) {
        log.debug("calll {}.register(), id = {}", this.getClass().getSimpleName(), itemDTO.getId());

        commandGateway.send(ItemRegisterCommand.builder()
                //.id(UUID.randomUUID().toString())
                .id(itemDTO.getId())
                .ownerId(SecurityUtils.getCurrentUserLogin())
                .title(itemDTO.getTitle())
                .category(itemDTO.getCategory())
                .targetPrice(itemDTO.getTargetPrice())
                .discountable(itemDTO.isDiscountable())
                .text(itemDTO.getText())
                .images(itemDTO.getImages())
                .status(SaleItemStatus.SALE)
                .createdDate(Instant.now())
                .build());
    }

    /**
     * Updates an existing item by sending ItemUpdateCommand through CommandGateway.
     * The current logged-in user is set as the owner.
     * Last modified date is set to current time.
     *
     * @param itemDTO the item data transfer object containing updated item details
     */
    @Override
    public void update(ItemDTO itemDTO) {
        log.debug("calll {}.update(), id = {}", this.getClass().getSimpleName(), itemDTO.getId());
        commandGateway.send(ItemUpdateCommand.builder()
                .id(itemDTO.getId())
                .ownerId(SecurityUtils.getCurrentUserLogin())
                .title(itemDTO.getTitle())
                .category(itemDTO.getCategory())
                .targetPrice(itemDTO.getTargetPrice())
                .discountable(itemDTO.isDiscountable())
                .text(itemDTO.getText())
                .images(itemDTO.getImages())
                .status(itemDTO.getStatus())
                .lastModifiedDate(Instant.now())
                .build());
    }

    /**
     * Deletes an item by its ID by sending ItemDeleteCommand through CommandGateway.
     *
     * @param id the ID of the item to delete
     */
    @Override
    public void delete(@PathVariable("id") String id) {
        log.debug("calll {}.delete({})", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemDeleteCommand.builder()
                .id(id)
                .build());
    }

    /**
     * Starts sale of the item by sending ItemSaleStartCommand through CommandGateway.
     *
     * @param id the ID of the item to start sale
     */
    @Override
    public void sale(String id) {
        log.debug("calll {}.sale(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleStartCommand.builder()
                .id(id)
                .build());
    }

    /**
     * Pauses sale of the item by sending ItemSaleStopCommand through CommandGateway.
     *
     * @param id the ID of the item to pause sale
     */
    @Override
    public void pause(String id) {
        log.debug("calll {}.pause(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleStopCommand.builder()
                .id(id)
                .build());
    }

    /**
     * Completes sale of the item by sending ItemSaleCompleteCommand through CommandGateway.
     *
     * @param id the ID of the item to complete sale
     */
    @Override
    public void complete(String id) {
        log.debug("calll {}.complete(), id = {}", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemSaleCompleteCommand.builder()
                .id(id)
                .build());
    }
}
