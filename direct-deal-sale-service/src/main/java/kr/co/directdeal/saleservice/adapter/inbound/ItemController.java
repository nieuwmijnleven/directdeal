package kr.co.directdeal.saleservice.adapter.inbound;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.lang.NonNull;
import kr.co.directdeal.saleservice.dto.ItemDTO;
import kr.co.directdeal.saleservice.service.ItemService;
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
    public void register(@Valid @RequestBody ItemDTO itemDTO) throws Exception {
        itemService.register(itemDTO);
    }

    @PutMapping 
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@Valid @RequestBody ItemDTO itemDTO) {
        itemService.update(itemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @NonNull String id) {
        itemService.delete(id);
    }
}
