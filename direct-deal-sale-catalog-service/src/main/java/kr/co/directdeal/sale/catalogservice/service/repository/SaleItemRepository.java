package kr.co.directdeal.sale.catalogservice.service.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.sale.catalogservice.domain.SaleItem;

@Repository
public interface SaleItemRepository extends MongoRepository<SaleItem, String> {
    public Optional<SaleItem> findById(String id);
}
