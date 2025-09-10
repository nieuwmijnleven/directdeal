package kr.co.directdeal.chattingservice.application.service.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kr.co.directdeal.chattingservice.domain.object.ChattingMessage;
import kr.co.directdeal.chattingservice.domain.object.ChattingRoom;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingRoomDTO;
import kr.co.directdeal.common.mapper.Mapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ChattingRoomMapper implements Mapper<ChattingRoom, ChattingRoomDTO> {

    private final Mapper<ChattingMessage, ChattingMessageDTO> chattingMessageMapper;

    @Override
    public ChattingRoom toEntity(ChattingRoomDTO dto) {
        return ChattingRoom.builder()
                    .id(dto.getId())
                    .title(dto.getTitle())
                    .itemId(dto.getItemId())
                    .sellerId(dto.getSellerId())
                    .customerId(dto.getCustomerId())
                    .messages(dto.getMessages()
                                    .stream()
                                    .map(chattingMessageMapper::toEntity)
                                    .collect(Collectors.toList())) 
                    .createdDate(dto.getCreatedDate())              
                    .build();
    }

    @Override
    public ChattingRoomDTO toDTO(ChattingRoom entity) {
        return ChattingRoomDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .itemId(entity.getItemId())
                    .sellerId(entity.getSellerId())
                    .customerId(entity.getCustomerId())
                    .messages(entity.getMessages()
                                    .stream()
                                    .map(chattingMessageMapper::toDTO)
                                    .collect(Collectors.toList())) 
                    .createdDate(entity.getCreatedDate())              
                    .build();
    }
}
