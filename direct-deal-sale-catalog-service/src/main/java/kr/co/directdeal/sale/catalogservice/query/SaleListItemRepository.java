package kr.co.directdeal.sale.catalogservice.query;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleListItemRepository extends MongoRepository<SaleListItem, String> {
    
}
