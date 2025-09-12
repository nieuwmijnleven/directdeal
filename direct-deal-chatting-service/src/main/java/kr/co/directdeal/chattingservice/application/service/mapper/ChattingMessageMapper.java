package kr.co.directdeal.chattingservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.chattingservice.domain.object.ChattingMessage;
import kr.co.directdeal.chattingservice.application.service.dto.ChattingMessageDTO;
import kr.co.directdeal.common.mapper.Mapper;

/**
 * Mapper to convert between ChattingMessage entity and ChattingMessageDTO.
 */
@Component
public class ChattingMessageMapper implements Mapper<ChattingMessage, ChattingMessageDTO> {

    /**
     * Converts ChattingMessageDTO to ChattingMessage entity.
     *
     * @param dto the ChattingMessageDTO object
     * @return ChattingMessage entity
     */
    @Override
    public ChattingMessage toEntity(ChattingMessageDTO dto) {
        return ChattingMessage.builder()
                .id(dto.getId())
                .chattingRoomId(dto.getChattingRoomId())
                .talkerId(dto.getTalkerId())
                .text(dto.getText())
                .createdDate(dto.getCreatedDate())
                .sent(dto.isSent())
                .build();
    }

    /**
     * Converts ChattingMessage entity to ChattingMessageDTO.
     *
     * @param entity the ChattingMessage entity
     * @return ChattingMessageDTO object
     */
    @Override
    public ChattingMessageDTO toDTO(ChattingMessage entity) {
        return ChattingMessageDTO.builder()
                .id(entity.getId())
                .chattingRoomId(entity.getChattingRoomId())
                .talkerId(entity.getTalkerId())
                .text(entity.getText())
                .createdDate(entity.getCreatedDate())
                .sent(entity.isSent())
                .build();
    }
}
