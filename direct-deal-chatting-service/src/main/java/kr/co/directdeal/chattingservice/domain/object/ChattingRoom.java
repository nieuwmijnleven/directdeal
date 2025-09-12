package kr.co.directdeal.chattingservice.domain.object;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
 * Represents a chatting room where users (seller and customer) can exchange messages.
 *
 * This entity maps to the CHATTING_ROOM table and maintains a unique index on itemId, sellerId, and customerId.
 */
@Entity
@Table(name = "CHATTING_ROOM",
        indexes = {
                @Index(unique = true, columnList = "CHATTING_ROOM_ITEM_ID, CHATTING_ROOM_SELLER_ID, CHATTING_ROOM_CUMSTOMER_ID")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ChattingRoom {

    /**
     * Unique identifier for the chatting room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATTING_ROOM_ID")
    private Long id;

    /**
     * Title of the chatting room.
     * Must be between 1 and 128 characters.
     */
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "CHATTING_ROOM_TITLE", length = 128, nullable = false)
    private String title;

    /**
     * Identifier for the related item.
     * Cannot be null.
     */
    @NotNull
    @Column(name = "CHATTING_ROOM_ITEM_ID", length = 36, nullable = false)
    private String itemId;

    /**
     * Seller's user ID (email).
     * Must be a valid email between 5 and 64 characters.
     */
    @Email
    @NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_ROOM_SELLER_ID", length = 50, nullable = false)
    private String sellerId;

    /**
     * Customer's user ID (email).
     * Must be a valid email between 5 and 64 characters.
     */
    @Email
    @NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_ROOM_CUMSTOMER_ID", length = 50, nullable = false)
    private String customerId;

    /**
     * List of messages associated with this chatting room.
     * Cascade all operations and remove orphan messages.
     */
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CHATTING_ROOM_ID")
    private List<ChattingMessage> messages = new ArrayList<>();

    /**
     * The date and time when this chatting room was created.
     * Set once on creation and not updated afterwards.
     */
    @CreatedDate
    @Column(name = "CHATTING_ROOM_CREATED_DATE", nullable = false, updatable = false)
    private Instant createdDate;

    /**
     * Adds a new message to this chatting room.
     *
     * @param message the message to add
     */
    public void addMessage(ChattingMessage message) {
        this.messages.add(message);
    }
}
