package kr.co.directdeal.sale.catalogservice.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.domain.SaleList;

@Repository
public interface SaleListRepository extends MongoRepository<SaleList, String> {
    public Page<SaleList> findAllByStatus(Pageable pageable, SaleItemStatus status);
}
