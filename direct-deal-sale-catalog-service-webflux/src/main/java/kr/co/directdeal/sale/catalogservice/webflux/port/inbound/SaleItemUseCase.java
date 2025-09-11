package kr.co.directdeal.sale.catalogservice.webflux.port.inbound;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SaleItemUseCase {
    Mono<SaleItemDTO> findSaleItemById(String id);

    Flux<SaleItemDTO> findSaleItemsByOnwerId(String ownerId);
}
