package kr.co.directdeal.chattingservice.port.inbound;

import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;

import java.util.List;

/**
 * Application service interface for chatting-related use cases.
 * Defines the contract for managing chatting rooms and messages.
 */
public interface ChattingUseCase {

    /**
     * Retrieves the list of chatting rooms associated with the given user ID
     * either as a seller or a customer.
     *
     * @param userId the ID of the user (seller or customer)
     * @return list of chatting room DTOs for the user
     */
    List<ChattingRoomDTO> getChattingRoomList(String userId);

    /**
     * Retrieves a chatting room based on the information provided in the DTO.
     * Also marks any unread messages as read for the current user.
     *
     * @param dto the chatting room DTO containing itemId, sellerId, and customerId
     * @return the chatting room DTO
     */
    ChattingRoomDTO getChattingRoom(ChattingRoomDTO dto);

    /**
     * Retrieves a chatting room by its unique ID.
     * Also marks any unread messages as read for the current user.
     *
     * @param id the ID of the chatting room
     * @return the chatting room DTO
     */
    ChattingRoomDTO getChattingRoom(Long id);

    /**
     * Creates a new chatting room.
     * Validates that the room does not already exist and the current user is a valid participant.
     *
     * @param dto the chatting room DTO to create
     * @return the created chatting room DTO
     */
    ChattingRoomDTO createChattingRoom(ChattingRoomDTO dto);

    /**
     * Sends a new message in a chatting room.
     * Validates the current user's permission.
     *
     * @param dto the chatting message DTO to send
     */
    void sendMessage(ChattingMessageDTO dto);

    /**
     * Fetches unread messages for the current user in a chatting room,
     * marks them as read, and returns them.
     *
     * @param dto the chatting message DTO containing the chatting room ID and talker ID
     * @return list of unread chatting message DTOs
     */
    List<ChattingMessageDTO> fetchUnreadMessage(ChattingMessageDTO dto);

    /**
     * Fetches messages from a chatting room starting after a given offset,
     * marks them as read, and returns them.
     *
     * @param chattingRoomId the ID of the chatting room
     * @param skip the number of messages to skip (offset)
     * @return list of chatting message DTOs
     */
    List<ChattingMessageDTO> fetchMessagesFrom(Long chattingRoomId, int skip);

    /**
     * Retrieves a list of customer IDs who have active chatting rooms for the given item ID.
     * The seller is identified based on the currently authenticated user.
     *
     * @param itemId the item ID for which to find customer IDs
     * @return list of customer IDs
     */
    List<String> getCustomerIdList(String itemId);
}
