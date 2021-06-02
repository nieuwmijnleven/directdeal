package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import javax.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.service.SaleItemQueryService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/saleitem")
public class SaleItemController {

    private final SaleItemQueryService saleItemQueryService;
    
    @GetMapping("/{id}")
    public SaleItemDTO get(@PathVariable("id") @NotBlank String id) {
        return saleItemQueryService.findSaleItemById(id);
    }
}
