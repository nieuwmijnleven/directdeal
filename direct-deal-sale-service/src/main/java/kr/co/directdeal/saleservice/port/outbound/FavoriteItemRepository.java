package kr.co.directdeal.saleservice.port.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.saleservice.domain.object.FavoriteItem;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    public Optional<FavoriteItem> findByUserIdAndItemId(String userId, String itemId);
    public List<FavoriteItem> findAllByUserId(String userId);
    public void deleteByUserIdAndItemId(String userId, String itemId);
}
