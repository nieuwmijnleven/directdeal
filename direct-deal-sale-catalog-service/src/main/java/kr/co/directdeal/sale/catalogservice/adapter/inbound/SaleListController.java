package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.service.SaleListCommandService;
import kr.co.directdeal.sale.catalogservice.service.SaleListQueryService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/salelist")
public class SaleListController {

    private final SaleListQueryService saleListQueryService;

    private final SaleListCommandService saleListCommandService;

    @GetMapping
    public List<SaleListItemDTO> list(Pageable pageable) {
        log.debug("pageable => " + pageable);
        return saleListQueryService.list(pageable);
    }

    @PutMapping("/{id}/lift-up")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> liftUp(@PathVariable("id") @NotBlank String id) {
        boolean result = saleListCommandService.liftUp(id);
        return Map.of("result", ((result) ? "success" : "false"));
    }
}
