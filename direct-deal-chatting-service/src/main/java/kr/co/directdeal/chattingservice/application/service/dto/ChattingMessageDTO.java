package kr.co.directdeal.chattingservice.application.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for a Chatting Message.
 *
 * Represents a message exchanged in a chatting room.
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChattingMessageDTO {

    /**
     * Unique identifier of the message.
     */
    private Long id;

    /**
     * Identifier of the chatting room this message belongs to.
     */
    private Long chattingRoomId;

    /**
     * ID of the user who sent the message.
     */
    private String talkerId;

    /**
     * Text content of the message.
     */
    private String text;

    /**
     * Timestamp when the message was created.
     */
    private Instant createdDate;

    /**
     * Indicates if the message has been sent successfully.
     */
    private boolean sent;
}
