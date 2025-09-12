package kr.co.directdeal.transactionhistoryservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.transactionhistoryservice.domain.object.BuyHistory;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.BuyHistoryDTO;

/**
 * Mapper for converting between {@link BuyHistory} entity and {@link BuyHistoryDTO} data transfer object.
 *
 * <p>This class provides methods to map the entity to DTO and vice versa.</p>
 *
 * @author Cheol
 */
@Component
public class BuyHistoryMapper implements Mapper<BuyHistory, BuyHistoryDTO> {

    /**
     * Converts a {@link BuyHistoryDTO} to a {@link BuyHistory} entity.
     *
     * @param dto the BuyHistoryDTO to convert
     * @return the converted BuyHistory entity
     */
    @Override
    public BuyHistory toEntity(BuyHistoryDTO dto) {
        return BuyHistory.builder()
                .id(dto.getId())
                .itemId(dto.getItemId())
                .buyerId(dto.getBuyerId())
                .title(dto.getTitle())
                .category(dto.getCategory())
                .targetPrice(dto.getTargetPrice())
                .mainImage(dto.getMainImage())
                .sellerId(dto.getSellerId())
                .completionTime(dto.getCompletionTime())
                .build();
    }

    /**
     * Converts a {@link BuyHistory} entity to a {@link BuyHistoryDTO}.
     *
     * @param entity the BuyHistory entity to convert
     * @return the converted BuyHistoryDTO
     */
    @Override
    public BuyHistoryDTO toDTO(BuyHistory entity) {
        return BuyHistoryDTO.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .buyerId(entity.getBuyerId())
                .title(entity.getTitle())
                .category(entity.getCategory())
                .targetPrice(entity.getTargetPrice())
                .mainImage(entity.getMainImage())
                .sellerId(entity.getSellerId())
                .completionTime(entity.getCompletionTime())
                .build();
    }
}
