package kr.co.directdeal.sale.catalogservice.webflux.port.outbound;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleList;
import reactor.core.publisher.Flux;

/**
 * Reactive repository interface for SaleList entities.
 * Provides method to retrieve SaleLists filtered by status with pagination support.
 *
 * @author Cheol Jeon
 */
@Repository
public interface SaleListRepository extends ReactiveCrudRepository<SaleList, String> {

    /**
     * Finds all SaleList items with the given status, paginated.
     *
     * @param pageable the pagination information
     * @param status the sale item status
     * @return a Flux emitting matching SaleList entities
     */
    Flux<SaleList> findAllByStatus(Pageable pageable, SaleItemStatus status);
}
