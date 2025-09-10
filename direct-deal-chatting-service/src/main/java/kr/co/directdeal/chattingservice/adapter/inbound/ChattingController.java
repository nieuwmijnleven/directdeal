package kr.co.directdeal.chattingservice.adapter.inbound;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.chattingservice.application.service.ChattingService;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;
import kr.co.directdeal.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingController {

    private final ChattingService chattingService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingRoomDTO> getChattingRoomList() {
        String userId = SecurityUtils.getCurrentUserLogin();
        return chattingService.getChattingRoomList(userId);
    }

    @GetMapping("/{itemId}/{sellerId}/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@PathVariable("itemId") String itemId, @PathVariable("sellerId") String sellerId, @PathVariable("customerId") String customerId) {
        ChattingRoomDTO dto = ChattingRoomDTO.builder()
                                    .itemId(itemId)
                                    .sellerId(sellerId)
                                    .customerId(customerId)
                                    .build();
        return chattingService.getChattingRoom(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@PathVariable("id") Long id) {
        return chattingService.getChattingRoom(id);
    }

    @GetMapping("/fetch-unread-messages")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingMessageDTO> fetchUnreadMessages(@RequestBody ChattingMessageDTO dto) {
        return chattingService.fetchUnreadMessage(dto);
    }

    @GetMapping("/{chattingRoomId}/fetch-from/{skip:\\d+}")
    @ResponseStatus(HttpStatus.OK)
    public List<ChattingMessageDTO> fetchMessagesFrom(@PathVariable("chattingRoomId") Long chattingRoomId, @PathVariable("skip") int skip) {
        return chattingService.fetchMessagesFrom(chattingRoomId, skip);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChattingRoomDTO createChattingRoom(@RequestBody ChattingRoomDTO dto) {
        return chattingService.createChattingRoom(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendMessage(@RequestBody ChattingMessageDTO dto) {
        chattingService.sendMessage(dto);
    }

    @GetMapping("/{itemId}/customer-list")
    @ResponseStatus(HttpStatus.OK)
    public List<String> customerList(@PathVariable("itemId") String itemId) {
        return chattingService.getCustomerIdList(itemId);
    }
}
