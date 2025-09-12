package kr.co.directdeal.sale.catalogservice.webflux.port.inbound;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Use case interface for SaleItem operations.
 * Provides methods to query sale items by ID or owner.
 *
 * @author Cheol Jeon
 */
public interface SaleItemUseCase {

    /**
     * Finds a sale item by its unique identifier.
     *
     * @param id the unique identifier of the sale item
     * @return a Mono emitting the SaleItemDTO if found, otherwise empty or error
     */
    Mono<SaleItemDTO> findSaleItemById(String id);

    /**
     * Finds all sale items owned by a specific owner.
     *
     * @param ownerId the identifier of the owner
     * @return a Flux emitting all SaleItemDTOs owned by the specified owner
     */
    Flux<SaleItemDTO> findSaleItemsByOnwerId(String ownerId);
}
