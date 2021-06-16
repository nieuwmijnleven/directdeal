package kr.co.directdeal.chattingservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(name = "CHATTING_ROOM_ITEM_ID", nullable = false)
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

