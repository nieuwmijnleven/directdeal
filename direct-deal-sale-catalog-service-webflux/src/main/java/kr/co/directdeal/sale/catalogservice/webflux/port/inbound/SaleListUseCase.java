package kr.co.directdeal.sale.catalogservice.webflux.port.inbound;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

/**
 * Use case interface for SaleList operations.
 * Provides methods to list sale items with pagination and to lift up a sale list.
 *
 * @author Cheol Jeon
 */
public interface SaleListUseCase {

    /**
     * Retrieves a paginated list of sale lists with status SALE.
     *
     * @param pageable the pagination information
     * @return a Flux emitting SaleListDTO objects according to the pageable settings
     */
    Flux<SaleListDTO> list(Pageable pageable);

    /**
     * Lifts up the sale list identified by the given id, updating its created date
     * if the lift up interval condition is met.
     *
     * @param id the identifier of the sale list to lift up
     * @return true if the lift up operation was successful; false otherwise
     */
    boolean liftUp(String id);
}
