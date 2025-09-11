package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

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

import kr.co.directdeal.saleservice.application.service.ItemCategoryService;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemCategoryDTO> list() {
        return itemCategoryService.list();
    }

    //@PreAuthority()
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void insert(@RequestBody ItemCategoryDTO dto) {
        log.debug("save => " + dto);
        itemCategoryService.insert(dto);
    }

    //@PreAuthority()
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody ItemCategoryDTO dto) {
        log.debug("update => " + dto);
        itemCategoryService.update(dto);
    }

    //@PreAuthority()
    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        itemCategoryService.delete(ItemCategoryDTO.builder()
                                        .id(id)
                                        .build());
    }
}
