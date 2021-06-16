package kr.co.directdeal.chattingservice.service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
public class ChattingRoomDTO {
    private Long id;
    private String itemId;
    private String sellerId;
    private String customerId;
    private List<ChattingMessageDTO> messages = new ArrayList<>();
    private Instant createdDate;
}
