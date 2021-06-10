package kr.co.directdeal.chattingservice.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "CHATTING_MESSAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ChattingMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHATTING_MESSAGE_ID", nullable = false)
    private String id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "CHATTING_ROOM_ID", insertable = false, updatable = false)
    // private ChattingRoom chattingRoom;

    // @ManyToOne
    // @JoinColumn(name = "CHATTING_ROOM_ID", insertable = false, updatable = false)

    @Column(name = "CHATTING_ROOM_ID", nullable = false)
    private String chattingRoomId;
    
    @NotNull
    @Column(name = "CHATTING_MESSAGE_TALKER_ID", nullable = false)
    private String talkerId;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "CHATTING_MESSAGE_TEXT", nullable = false)
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
