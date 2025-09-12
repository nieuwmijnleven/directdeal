package kr.co.directdeal.chattingservice.domain.object;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a message in a chatting room.
 *
 * This entity maps to the CHATTING_MESSAGE table.
 */
@Entity
@Table(name = "CHATTING_MESSAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ChattingMessage {

    /**
     * The unique identifier for the chatting message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATTING_MESSAGE_ID")
    private Long id;

    /**
     * The ID of the chatting room this message belongs to.
     */
    @Column(name = "CHATTING_ROOM_ID", nullable = false)
    private Long chattingRoomId;

    /**
     * The ID of the user who sent the message.
     * Must be a valid email address between 5 and 64 characters.
     */
    @Email
    @NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_MESSAGE_TALKER_ID", length = 50, nullable = false)
    private String talkerId;

    /**
     * The text content of the chatting message.
     * Must be between 1 and 512 characters.
     */
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "CHATTING_MESSAGE_TEXT", length = 512, nullable = false)
    private String text;

    /**
     * The timestamp when this message was created.
     */
    @NotNull
    @Column(name = "CHATTING_MESSAGE_CREATED_DATE", nullable = false)
    @CreatedDate
    private Instant createdDate;

    /**
     * Indicates whether this message has been sent (read) by the recipient.
     */
    @Column(name = "CHATTING_MESSAGE_SENT", nullable = false)
    private boolean sent;

    /**
     * Returns true if the message has NOT been sent/read yet.
     *
     * @return true if the message is not sent, false otherwise
     */
    public boolean isNotSent() {
        return !sent;
    }
}
