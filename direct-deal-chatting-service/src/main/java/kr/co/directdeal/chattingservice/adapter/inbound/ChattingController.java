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

import kr.co.directdeal.chattingservice.service.ChattingRoomService;
import kr.co.directdeal.chattingservice.service.dto.ChattingRoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingController {

    private final ChattingRoomService chattingRoomService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@RequestBody ChattingRoomDTO dto) {
        return chattingRoomService.getChattingRoom(dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ChattingRoomDTO getChattingRoom(@PathVariable("id") String id) {
        return chattingRoomService.getChattingRoom(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChattingRoomDTO createChattingRoom(@RequestBody ChattingRoomDTO dto) {
        return chattingRoomService.createChattingRoom(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void sendChattingMessage(@RequestBody ChattingRoomDTO dto) {
        chattingRoomService.sendChattingMessage(dto);
    }

    @GetMapping("/{itemId}/customer-list")
    @ResponseStatus(HttpStatus.OK)
    public List<String> customerList(@PathVariable("itemId") String itemId) {
        return chattingRoomService.getCustomerIdList(itemId);
    }
}
