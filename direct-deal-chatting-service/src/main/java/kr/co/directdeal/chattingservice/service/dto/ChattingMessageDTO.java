package kr.co.directdeal.chattingservice.service.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChattingMessageDTO {
    private String id;
    private String talkerId;
    private String text;
    private Instant createdDate;
}
