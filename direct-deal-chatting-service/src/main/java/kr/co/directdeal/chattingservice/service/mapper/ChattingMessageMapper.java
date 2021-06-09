package kr.co.directdeal.chattingservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.chattingservice.domain.ChattingMessage;
import kr.co.directdeal.chattingservice.service.dto.ChattingMessageDTO;
import kr.co.directdeal.common.mapper.Mapper;

@Component
public class ChattingMessageMapper implements Mapper<ChattingMessage, ChattingMessageDTO> {

    @Override
    public ChattingMessage toEntity(ChattingMessageDTO dto) {
        return ChattingMessage.builder()
                    .id(dto.getId())
                    .talkerId(dto.getTalkerId())
                    .text(dto.getText())
                    .createdDate(dto.getCreatedDate())
                    .build();
    }

    @Override
    public ChattingMessageDTO toDTO(ChattingMessage entity) {
        return ChattingMessageDTO.builder()
                    .id(entity.getId())
                    .talkerId(entity.getTalkerId())
                    .text(entity.getText())
                    .createdDate(entity.getCreatedDate())
                    .build();
    }
}
