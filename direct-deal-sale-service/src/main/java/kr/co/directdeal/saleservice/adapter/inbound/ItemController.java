package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.UUID;

import javax.validation.Valid;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.sale.command.ItemDeleteCommand;
import kr.co.directdeal.common.sale.command.ItemRegisterCommand;
import kr.co.directdeal.common.sale.command.ItemUpdateCommand;
import kr.co.directdeal.saleservice.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final CommandGateway commandGateway;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String get() {
        return "1";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody ItemDTO itemDTO) {
        commandGateway.send(ItemRegisterCommand.builder()
                                .id(UUID.randomUUID().toString())
                                .title(itemDTO.getTitle())
                                .category(itemDTO.getCategory())
                                .targetPrice(itemDTO.getTargetPrice())
                                .text(itemDTO.getText())
                                .imagePath(itemDTO.getImagePath())
                                .status(itemDTO.getStatus())
                                .build());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@Valid @RequestBody ItemDTO itemDTO) {
        log.debug("calll {}.update(), id = {}", this.getClass().getSimpleName(), itemDTO.getId());
        commandGateway.send(ItemUpdateCommand.builder()
                                .id(itemDTO.getId())
                                .title(itemDTO.getTitle())
                                .category(itemDTO.getCategory())
                                .targetPrice(itemDTO.getTargetPrice())
                                .text(itemDTO.getText())
                                .imagePath(itemDTO.getImagePath())
                                .status(itemDTO.getStatus())
                                .build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        log.debug("calll {}.delete({})", this.getClass().getSimpleName(), id);
        commandGateway.send(ItemDeleteCommand.builder()
                                .id(id)
                                .build());
    }
}
