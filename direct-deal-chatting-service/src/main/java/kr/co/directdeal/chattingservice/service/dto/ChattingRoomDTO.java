package kr.co.directdeal.chattingservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChattingRoomDTO {
    private Long id;
    private String title;
    private String itemId;
    private String sellerId;
    private String customerId;
    @Builder.Default
    private List<ChattingMessageDTO> messages = new ArrayList<>();
    private Instant createdDate;
}
