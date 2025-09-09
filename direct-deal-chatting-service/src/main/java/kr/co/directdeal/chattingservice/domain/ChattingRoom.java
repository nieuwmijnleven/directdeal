package kr.co.directdeal.chattingservice.domain;

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATTING_ROOM_ID")
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "CHATTING_ROOM_TITLE", length = 128, nullable = false)
    private String title;

    @NotNull
    @Column(name = "CHATTING_ROOM_ITEM_ID", length = 36, nullable = false)
    private String itemId;

    @Email
	@NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_ROOM_SELLER_ID", length = 50, nullable = false)
    private String sellerId;

    @Email
	@NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_ROOM_CUMSTOMER_ID", length = 50, nullable = false)
    private String customerId;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "CHATTING_ROOM_ID")
    private List<ChattingMessage> messages = new ArrayList<>();

    @CreatedDate
    @Column(name = "CHATTING_ROOM_CREATED_DATE", nullable = false, updatable = false)
    private Instant createdDate;

    public void addMessage(ChattingMessage message) {
        this.messages.add(message);
    }
}

