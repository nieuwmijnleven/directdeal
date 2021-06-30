package kr.co.directdeal.sale.catalogservice.webflux.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.SaleList;
import reactor.core.publisher.Flux;

@Repository
public interface SaleListRepository extends ReactiveCrudRepository<SaleList, String> {
    public Flux<SaleList> findAllByStatus(Pageable pageable, SaleItemStatus status);
}
