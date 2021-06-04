package kr.co.directdeal.sale.catalogservice.query;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, String> {
    @EntityGraph(attributePaths = {"images"}, type = EntityGraphType.LOAD)
    public Optional<SaleItem> findById(String id);
}
