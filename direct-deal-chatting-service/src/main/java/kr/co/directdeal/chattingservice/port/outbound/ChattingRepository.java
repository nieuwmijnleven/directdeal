package kr.co.directdeal.chattingservice.port.outbound;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.co.directdeal.chattingservice.domain.object.ChattingRoom;

/**
 * Repository interface for accessing ChattingRoom entities from the database.
 * Extends JpaRepository to provide basic CRUD and query operations.
 */
@Repository
public interface ChattingRepository extends JpaRepository<ChattingRoom, Long> {

    /**
     * Finds a chatting room by the specified item ID, seller ID, and customer ID.
     *
     * @param itemId the ID of the item
     * @param sellerId the ID of the seller
     * @param customerId the ID of the customer
     * @return an Optional containing the ChattingRoom if found, or empty if not found
     */
    Optional<ChattingRoom> findByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);

    /**
     * Finds chatting rooms by seller ID and item ID.
     *
     * @param sellerId the ID of the seller
     * @param itemId the ID of the item
     * @return a list of ChattingRoom entities matching the criteria
     */
    List<ChattingRoom> findBySellerIdAndItemId(String sellerId, String itemId);

    /**
     * Finds chatting rooms where the given user ID is either the seller or the customer.
     *
     * @param sellerId the seller ID to match
     * @param customerId the customer ID to match
     * @return a list of ChattingRoom entities where either sellerId or customerId matches
     */
    List<ChattingRoom> findBySellerIdOrCustomerId(String sellerId, String customerId);

    /**
     * Checks if a chatting room exists for the given item ID, seller ID, and customer ID.
     *
     * @param itemId the ID of the item
     * @param sellerId the ID of the seller
     * @param customerId the ID of the customer
     * @return true if such a chatting room exists, false otherwise
     */
    boolean existsByItemIdAndSellerIdAndCustomerId(String itemId, String sellerId, String customerId);
}
