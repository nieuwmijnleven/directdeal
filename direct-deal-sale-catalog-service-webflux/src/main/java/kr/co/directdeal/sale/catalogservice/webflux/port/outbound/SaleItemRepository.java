package kr.co.directdeal.sale.catalogservice.webflux.port.outbound;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive repository interface for SaleItem entities.
 * Provides methods to retrieve SaleItems by id or by ownerId and status.
 *
 * @author Cheol Jeon
 */
@Repository
public interface SaleItemRepository extends ReactiveCrudRepository<SaleItem, String> {

    /**
     * Finds a SaleItem by its identifier.
     *
     * @param id the id of the SaleItem
     * @return a Mono emitting the SaleItem if found, or empty if not
     */
    Mono<SaleItem> findById(String id);

    /**
     * Finds all SaleItems for a given owner and with a specific status.
     *
     * @param ownerId the owner id
     * @param status the sale item status
     * @return a Flux emitting matching SaleItems
     */
    Flux<SaleItem> findByOwnerIdAndStatus(String ownerId, SaleItemStatus status);
}
