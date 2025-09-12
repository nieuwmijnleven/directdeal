package kr.co.directdeal.saleservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.FavoriteItem;
import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;

/**
 * Mapper class to convert between FavoriteItem entity and FavoriteItemDTO.
 * Implements methods toEntity and toDTO for data transformation.
 *
 * @author Cheol Jeon
 */
@Component
public class FavoriteItemMapper implements Mapper<FavoriteItem, FavoriteItemDTO> {

    /**
     * Converts FavoriteItemDTO to FavoriteItem entity.
     *
     * @param dto the FavoriteItemDTO to convert
     * @return the corresponding FavoriteItem entity
     */
    @Override
    public FavoriteItem toEntity(FavoriteItemDTO dto) {
        return FavoriteItem.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .createdDate(dto.getCreatedDate())
                .build();
    }

    /**
     * Converts FavoriteItem entity to FavoriteItemDTO.
     *
     * @param entity the FavoriteItem entity to convert
     * @return the corresponding FavoriteItemDTO
     */
    @Override
    public FavoriteItemDTO toDTO(FavoriteItem entity) {
        return FavoriteItemDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .itemId(entity.getItemId())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
