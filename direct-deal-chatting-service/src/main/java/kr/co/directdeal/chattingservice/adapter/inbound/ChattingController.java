package kr.co.directdeal.chattingservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import kr.co.directdeal.chattingservice.application.service.ChattingService;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;
import kr.co.directdeal.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller for managing chatting-related endpoints.
 *
 * Provides APIs to create and retrieve chatting rooms,
 * send and fetch messages, and get customer lists.
 *
 * All endpoints require user authentication (except where explicitly permitted).
 * The current user is extracted from the security context.
 *
 * Base URL: /chatting
 *
 * @author
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingController {

    private final ChattingService chattingService;

    /**
     * Retrieves the list of chatting rooms for the current authenticated user.
     *
     * @return list of ChattingRoomDTO objects
     */
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingRoomDTO> getChattingRoomList() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return chattingService.getChattingRoomList(userId);
    }

    /**
     * Retrieves a chatting room by composite key: itemId, sellerId, and customerId.
     *
     * @param itemId the ID of the item
     * @param sellerId the seller's user ID
     * @param customerId the customer's user ID
     * @return ChattingRoomDTO representing the chatting room
     */
    @GetMapping("/{itemId}/{sellerId}/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@PathVariable("itemId") String itemId,
                                           @PathVariable("sellerId") String sellerId,
                                           @PathVariable("customerId") String customerId) {
        ChattingRoomDTO dto = ChattingRoomDTO.builder()
                .itemId(itemId)
                .sellerId(sellerId)
                .customerId(customerId)
                .build();
        return chattingService.getChattingRoom(dto);
    }

    /**
     * Retrieves a chatting room by its ID.
     *
     * @param id the chatting room ID
     * @return ChattingRoomDTO representing the chatting room
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@PathVariable("id") Long id) {
        return chattingService.getChattingRoom(id);
    }

    /**
     * Fetches unread messages based on the criteria provided in the ChattingMessageDTO.
     *
     * @param dto the DTO containing criteria to fetch unread messages
     * @return list of unread ChattingMessageDTO objects
     */
    @GetMapping("/fetch-unread-messages")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingMessageDTO> fetchUnreadMessages(@RequestBody ChattingMessageDTO dto) {
        return chattingService.fetchUnreadMessage(dto);
    }

    /**
     * Fetches messages from a chatting room, skipping the specified number of messages.
     * Used for pagination or lazy loading.
     *
     * @param chattingRoomId the ID of the chatting room
     * @param skip number of messages to skip
     * @return list of ChattingMessageDTO objects starting from the skip offset
     */
    @GetMapping("/{chattingRoomId}/fetch-from/{skip:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingMessageDTO> fetchMessagesFrom(@PathVariable("chattingRoomId") Long chattingRoomId,
                                                      @PathVariable("skip") int skip) {
        return chattingService.fetchMessagesFrom(chattingRoomId, skip);
    }

    /**
     * Creates a new chatting room.
     *
     * @param dto the ChattingRoomDTO containing information to create the room
     * @return the created ChattingRoomDTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChattingRoomDTO createChattingRoom(@RequestBody ChattingRoomDTO dto) {
        return chattingService.createChattingRoom(dto);
    }

    /**
     * Sends a message in a chatting room.
     *
     * @param dto the ChattingMessageDTO containing message details
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@RequestBody ChattingMessageDTO dto) {
        chattingService.sendMessage(dto);
    }

    /**
     * Retrieves a list of customer IDs associated with the specified item.
     *
     * @param itemId the ID of the item
     * @return list of customer IDs as strings
     */
    @GetMapping("/{itemId}/customer-list")
    @ResponseStatus(HttpStatus.OK)
    public List<String> customerList(@PathVariable("itemId") String itemId) {
        return chattingService.getCustomerIdList(itemId);
    }
}
