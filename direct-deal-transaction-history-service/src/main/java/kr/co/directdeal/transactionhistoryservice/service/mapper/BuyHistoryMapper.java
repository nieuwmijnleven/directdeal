package kr.co.directdeal.transactionhistoryservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.BuyHistory;
import kr.co.directdeal.transactionhistoryservice.service.dto.BuyHistoryDTO;

@Component
public class BuyHistoryMapper implements Mapper<BuyHistory, BuyHistoryDTO> {

    @Override
    public BuyHistory toEntity(BuyHistoryDTO dto) {
        return BuyHistory.builder()
                    .id(dto.getId())
                    .itemId(dto.getItemId())
                    .buyerId(dto.getBuyerId())
                    .title(dto.getTitle())
                    .category(dto.getCategory())
                    .targetPrice(dto.getTargetPrice())
                    .sellerId(dto.getSellerId())
                    .completionTime(dto.getCompletionTime())
                    .build();
    }

    @Override
    public BuyHistoryDTO toDTO(BuyHistory entity) {
        return BuyHistoryDTO.builder()
                    .id(entity.getId())
                    .itemId(entity.getItemId())
                    .buyerId(entity.getBuyerId())
                    .title(entity.getTitle())
                    .category(entity.getCategory())
                    .targetPrice(entity.getTargetPrice())
                    .sellerId(entity.getSellerId())
                    .completionTime(entity.getCompletionTime())
                    .build();
    }
}
