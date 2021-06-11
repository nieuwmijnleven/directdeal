package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import javax.validation.constraints.NotBlank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/saleitem")
public class SaleItemController {

    private final SaleItemService saleItemService;
    
    @GetMapping("/{id}")
    public SaleItemDTO getSaleItem(@PathVariable("id") @NotBlank String id) {
        return saleItemService.findSaleItemById(id);
    }
}
