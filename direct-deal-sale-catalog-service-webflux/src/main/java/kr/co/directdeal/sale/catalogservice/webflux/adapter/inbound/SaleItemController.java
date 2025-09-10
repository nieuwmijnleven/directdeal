package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/saleitem")
public class SaleItemController {

    private final SaleItemService saleItemService;
    
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<SaleItemDTO> getSaleItem(@PathVariable("id") @NotBlank String id) {
        return saleItemService.findSaleItemById(id);
    }

    @GetMapping("/seller-items")
    @ResponseStatus(HttpStatus.OK)
    public Flux<SaleItemDTO> getSellerSaleItems() {
        return ReactiveSecurityContextHolder.getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(Authentication::getName)
                    .flatMapMany(saleItemService::findSaleItemsByOnwerId);
    }
}
