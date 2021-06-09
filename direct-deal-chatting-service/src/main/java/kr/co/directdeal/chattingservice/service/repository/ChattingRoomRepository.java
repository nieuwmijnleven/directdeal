package kr.co.directdeal.chattingservice.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.chattingservice.domain.ChattingRoom;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, String> {
    public Optional<ChattingRoom> findByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);
    public List<ChattingRoom> findBySellerIdAndItemId(String sellerId, String itemId);
    public boolean existsByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);
}
