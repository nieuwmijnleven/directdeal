package kr.co.directdeal.chattingservice.port.inbound;

import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;

import java.util.List;

public interface ChattingUseCase {
    List<ChattingRoomDTO> getChattingRoomList(String userId);

    ChattingRoomDTO getChattingRoom(ChattingRoomDTO dto);

    ChattingRoomDTO getChattingRoom(Long id);

    ChattingRoomDTO createChattingRoom(ChattingRoomDTO dto);

    void sendMessage(ChattingMessageDTO dto);

    List<ChattingMessageDTO> fetchUnreadMessage(ChattingMessageDTO dto);

    List<ChattingMessageDTO> fetchMessagesFrom(Long chattingRoomId, int skip);

    List<String> getCustomerIdList(String itemId);
}
