package kr.co.directdeal.saleservice.application.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.FavoriteItem;
import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;

@Component
public class FavoriteItemMapper implements Mapper<FavoriteItem, FavoriteItemDTO> {

	@Override
	public FavoriteItem toEntity(FavoriteItemDTO dto) {
		return FavoriteItem.builder()
					.id(dto.getId())
					.userId(dto.getUserId())
					.itemId(dto.getItemId())
					.createdDate(dto.getCreatedDate())
					.build();
	}

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
