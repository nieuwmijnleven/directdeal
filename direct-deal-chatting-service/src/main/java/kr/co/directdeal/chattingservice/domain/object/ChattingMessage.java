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

@Entity
@Table(name = "CHATTING_MESSAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ChattingMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATTING_MESSAGE_ID")
    private Long id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "CHATTING_ROOM_ID", insertable = false, updatable = false)
    // private ChattingRoom chattingRoom;

    @Column(name = "CHATTING_ROOM_ID", nullable = false)
    private Long chattingRoomId;
    
    @Email
	@NotNull
    @Size(min = 5, max = 64)
    @Column(name = "CHATTING_MESSAGE_TALKER_ID", length = 50, nullable = false)
    private String talkerId;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "CHATTING_MESSAGE_TEXT", length = 512, nullable = false)
    private String text;

    @NotNull
    @Column(name = "CHATTING_MESSAGE_CREATED_DATE", nullable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "CHATTING_MESSAGE_SENT", nullable = false)
    private boolean sent;

    public boolean isNotSent() {
        return !sent;
    }
}
