package kr.co.directdeal.chattingservice.port.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.chattingservice.domain.object.ChattingRoom;

@Repository
public interface ChattingRepository extends JpaRepository<ChattingRoom, Long> {
    public Optional<ChattingRoom> findByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);
    public List<ChattingRoom> findBySellerIdAndItemId(String sellerId, String itemId);
    public List<ChattingRoom> findBySellerIdOrCustomerId(String sellerId, String customerId);
    public boolean existsByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);
}
