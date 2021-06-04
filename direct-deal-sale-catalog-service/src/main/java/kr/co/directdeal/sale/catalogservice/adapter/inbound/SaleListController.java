package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.dto.SaleListItemDTO;
import kr.co.directdeal.sale.catalogservice.service.SaleListQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/salelist")
public class SaleListController {

    private final SaleListQueryService saleListQueryService;
    
    @GetMapping("/{id}")
    public SaleListItemDTO get(@PathVariable("id") @NotBlank String id) {
        return saleListQueryService.findSaleListItemById(id);
    }

    @GetMapping
    public List<SaleListItemDTO> list(Pageable pageable) {
        log.debug("pageable => " + pageable);
        return saleListQueryService.list(pageable);
    }
}
