package kr.co.directdeal.sale.catalogservice.service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.sale.catalogservice.domain.SaleListItem;

@Repository
public interface SaleListItemRepository extends MongoRepository<SaleListItem, String> {
    
}
