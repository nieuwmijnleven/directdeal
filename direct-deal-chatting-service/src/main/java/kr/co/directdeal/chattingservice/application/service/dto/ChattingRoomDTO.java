package kr.co.directdeal.chattingservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a Chatting Room.
 *
 * Represents a chat room where messages are exchanged between
 * a seller and a customer regarding an item.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChattingRoomDTO {

    /**
     * Unique identifier of the chatting room.
     */
    private Long id;

    /**
     * Title or name of the chatting room.
     */
    private String title;

    /**
     * Identifier of the item related to this chat room.
     */
    private String itemId;

    /**
     * User ID of the seller involved in this chat.
     */
    private String sellerId;

    /**
     * User ID of the customer involved in this chat.
     */
    private String customerId;

    /**
     * List of messages exchanged in this chat room.
     * Defaults to an empty list if none exist.
     */
    @Builder.Default
    private List<ChattingMessageDTO> messages = new ArrayList<>();

    /**
     * Timestamp when the chat room was created.
     */
    private Instant createdDate;
}
