package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.adapter.inbound.result.ItemRegistrationResult;
import kr.co.directdeal.saleservice.service.ItemService;
import kr.co.directdeal.saleservice.service.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRegistrationResult register(@Valid @RequestBody ItemDTO itemDTO) {
        String itemId = UUID.randomUUID().toString();
        String userId = SecurityUtils.getCurrentUserLogin();

        itemDTO.setId(itemId);
        itemDTO.setOwnerId(userId);
        itemService.register(itemDTO);

        return ItemRegistrationResult.builder()
                    .itemId(itemId)
                    .build();
    }

    @PutMapping 
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@Valid @RequestBody ItemDTO itemDTO) {
        itemService.update(itemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @NotBlank String id) {
        itemService.delete(id);
    }

    @PutMapping("/{id}/sale")
    @ResponseStatus(HttpStatus.CREATED)
    public void sale(@PathVariable("id") @NotBlank String id) {
        itemService.sale(id);
    }

    @PutMapping("/{id}/pause")
    @ResponseStatus(HttpStatus.CREATED)
    public void pause(@PathVariable("id") @NotBlank String id) {
        itemService.pause(id);
    }

    @PutMapping("/{id}/complete")
    @ResponseStatus(HttpStatus.CREATED)
    public void complete(@PathVariable("id") @NotBlank String id) {
        itemService.complete(id);
    }
}
