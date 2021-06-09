package kr.co.directdeal.chattingservice.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.chattingservice.domain.ChattingMessage;
import kr.co.directdeal.chattingservice.domain.ChattingRoom;
import kr.co.directdeal.chattingservice.exception.ChattingException;
import kr.co.directdeal.chattingservice.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.service.dto.ChattingRoomDTO;
import kr.co.directdeal.chattingservice.service.repository.ChattingRoomRepository;
import kr.co.directdeal.chattingservice.temp.SecurityUtils;
import kr.co.directdeal.common.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingRoomService {
    
    private final ChattingRoomRepository chattingRoomRepository;

    private final Mapper<ChattingRoom, ChattingRoomDTO> chattingRoomMapper;

    private final Mapper<ChattingMessage, ChattingMessageDTO> chattingMessageMapper;

    @Transactional(readOnly = true)
    public ChattingRoomDTO getChattingRoom(ChattingRoomDTO dto) {
        ChattingRoom chattingRoom = findChattingRoom(dto);    
        checkValidTalker(chattingRoom);
        return chattingRoomMapper.toDTO(chattingRoom);
    }

    @Transactional(readOnly = true)
    public ChattingRoomDTO getChattingRoom(String id) {
        ChattingRoom chattingRoom = findChattingRoomById(id);     
        checkValidTalker(chattingRoom);                       
        return chattingRoomMapper.toDTO(chattingRoom);
    }

    @Transactional
    public ChattingRoomDTO createChattingRoom(ChattingRoomDTO dto) {
        //check if the chatting room has already created
        checkAlreadyCreated(dto);

        //check if valid talkers
        ChattingRoom newChattingRoom = chattingRoomMapper.toEntity(dto);
        checkValidTalker(newChattingRoom);  
 
        newChattingRoom.setCreatedDate(Instant.now());
        newChattingRoom = chattingRoomRepository.save(newChattingRoom);
        return chattingRoomMapper.toDTO(newChattingRoom);
    }

    @Transactional
    public void sendChattingMessage(ChattingRoomDTO dto) {
        ChattingRoom chattingRoom = findChattingRoomById(dto.getId());
        checkValidTalker(chattingRoom);

        ChattingMessageDTO chattingMessageDTO = dto.getNewMessage();
        chattingMessageDTO.setCreatedDate(Instant.now());
        chattingRoom.addMessage(chattingMessageMapper.toEntity(chattingMessageDTO));                                    
    }

    @Transactional(readOnly = true)
    public List<String> getCustomerIdList(String itemId) {
        String sellerId = SecurityUtils.getCurrentUserLogin();
        return chattingRoomRepository
                    .findBySellerIdAndItemId(sellerId, itemId)
                    .stream()
                    .map(ChattingRoom::getCustomerId)
                    .collect(Collectors.toList());                             
    }

    private ChattingRoom findChattingRoom(ChattingRoomDTO dto) {
        return chattingRoomRepository
                    .findByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId())
                    .orElseThrow(() -> ChattingException.builder()
                                            .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.notfound.message")
                                            .messageArgs(new String[]{ dto.getItemId(), dto.getSellerId(), dto.getCustomerId() })
                                            .build());
    }

    private ChattingRoom findChattingRoomById(String id) {
        return chattingRoomRepository
                    .findById(id)
                    .orElseThrow(() -> ChattingException.builder()
                                            .messageKey("chattingroomservice.exception.sendchattingmessage.chattingroom.notfound.message")
                                            .messageArgs(new String[]{id})
                                            .build());
    }

    private void checkValidTalker(ChattingRoom chattingRoom) {
        String userId = SecurityUtils.getCurrentUserLogin();
        if (!Objects.equals(userId, chattingRoom.getSellerId()) 
            && !Objects.equals(userId, chattingRoom.getCustomerId()))
            throw ChattingException.builder()
                    .messageKey("chattingroomservice.exception.findchattingroom.chattingroom.invalidchattinguser.message")
                    .messageArgs(new String[]{})
                    .build();
    }

    private void checkAlreadyCreated(ChattingRoomDTO dto) {
        boolean hasAlreadyCreated = 
            chattingRoomRepository
                .existsByItemIdAndSellerIdAndCustomerId(dto.getItemId(), dto.getSellerId(), dto.getCustomerId());

        if (hasAlreadyCreated)
            throw ChattingException.builder()
                    .messageKey("chattingroomservice.exception.createchattingroom.chattingroom.hasalreadycreated.message")
                    .messageArgs(new String[]{})
                    .build();
    }
}
