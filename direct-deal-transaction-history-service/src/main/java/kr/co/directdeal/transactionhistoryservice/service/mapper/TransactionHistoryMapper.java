package kr.co.directdeal.transactionhistoryservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.TransactionHistory;
import kr.co.directdeal.transactionhistoryservice.service.dto.TransactionHistoryDTO;

@Component
public class TransactionHistoryMapper implements Mapper<TransactionHistory, TransactionHistoryDTO> {

    @Override
    public TransactionHistory toEntity(TransactionHistoryDTO dto) {
        return TransactionHistory.builder()
                    .id(dto.getId())
                    .itemId(dto.getItemId())
                    .sellerId(dto.getSellerId())
                    .title(dto.getTitle())
                    .category(dto.getCategory())
                    .targetPrice(dto.getTargetPrice())
                    .mainImage(dto.getMainImage())
                    .buyerId(dto.getBuyerId())
                    .completionTime(dto.getCompletionTime())
                    .build();
    }

    @Override
    public TransactionHistoryDTO toDTO(TransactionHistory entity) {
        return TransactionHistoryDTO.builder()
                    .id(entity.getId())
                    .itemId(entity.getItemId())
                    .sellerId(entity.getSellerId())
                    .title(entity.getTitle())
                    .category(entity.getCategory())
                    .targetPrice(entity.getTargetPrice())
                    .mainImage(entity.getMainImage())
                    .buyerId(entity.getBuyerId())
                    .completionTime(entity.getCompletionTime())
                    .build();
    }
}
