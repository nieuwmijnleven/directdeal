package kr.co.directdeal.saleservice.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.ItemCategory;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, String> {
    List<ItemCategory> findByParentIsNull();
}
