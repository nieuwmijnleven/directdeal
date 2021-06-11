package kr.co.directdeal.saleservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.saleservice.service.FavoriteItemService;
import kr.co.directdeal.saleservice.service.dto.FavoriteItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/favorite")
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FavoriteItemDTO> list() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return favoriteItemService.list(FavoriteItemDTO.builder()
                                            .userId(userId)
                                            .build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody FavoriteItemDTO dto) {
        String userId = SecurityUtils.getCurrentUserLogin();
        dto.setUserId(userId);
        log.debug("save => " + dto);
        favoriteItemService.save(dto);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("itemId") String itemId) {
        String userId = SecurityUtils.getCurrentUserLogin();
        favoriteItemService.delete(FavoriteItemDTO.builder()
                                        .userId(userId)
                                        .itemId(itemId)
                                        .build());
    }
}
