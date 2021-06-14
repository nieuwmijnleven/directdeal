package kr.co.directdeal.sale.catalogservice.service.repository;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.sale.catalogservice.domain.SaleItem;

@Repository
public interface SaleItemRepository extends MongoRepository<SaleItem, String> {
    @Cacheable(cacheNames = "SaleItemCache", key="#p0") //key="#id" is not working. https://github.com/spring-projects/spring-framework/issues/13406
    public Optional<SaleItem> findById(String id);
}
