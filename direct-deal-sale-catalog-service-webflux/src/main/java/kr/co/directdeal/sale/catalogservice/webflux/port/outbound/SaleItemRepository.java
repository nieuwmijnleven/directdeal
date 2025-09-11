package kr.co.directdeal.sale.catalogservice.webflux.port.outbound;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SaleItemRepository extends ReactiveCrudRepository<SaleItem, String> {
    // @Cacheable(cacheNames = "SaleItemCache", key = "#p0", unless = "#result == null") //key="#id" is not working. https://github.com/spring-projects/spring-framework/issues/13406
    public Mono<SaleItem> findById(String id);
    public Flux<SaleItem> findByOwnerIdAndStatus(String ownerId, SaleItemStatus status);
}
