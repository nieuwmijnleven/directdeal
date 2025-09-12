package kr.co.directdeal.transactionhistoryservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.object.TransactionHistory;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;

/**
 * Mapper for converting between {@link TransactionHistory} entity and {@link TransactionHistoryDTO} data transfer object.
 *
 * <p>This class provides methods to map the entity to DTO and vice versa.</p>
 *
 * @author Cheol
 */
@Component
public class TransactionHistoryMapper implements Mapper<TransactionHistory, TransactionHistoryDTO> {

    /**
     * Converts a {@link TransactionHistoryDTO} to a {@link TransactionHistory} entity.
     *
     * @param dto the TransactionHistoryDTO to convert
     * @return the converted TransactionHistory entity
     */
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

    /**
     * Converts a {@link TransactionHistory} entity to a {@link TransactionHistoryDTO}.
     *
     * @param entity the TransactionHistory entity to convert
     * @return the converted TransactionHistoryDTO
     */
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
