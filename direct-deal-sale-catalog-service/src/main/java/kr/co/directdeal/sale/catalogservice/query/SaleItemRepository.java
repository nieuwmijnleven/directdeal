package kr.co.directdeal.sale.catalogservice.query;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends MongoRepository<SaleItem, String> {
    public Optional<SaleItem> findById(String id);
}
