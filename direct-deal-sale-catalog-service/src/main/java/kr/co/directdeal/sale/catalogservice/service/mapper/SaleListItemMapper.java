package kr.co.directdeal.sale.catalogservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleListItem;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListItemDTO;

@Component
public class SaleListItemMapper implements Mapper<SaleListItem, SaleListItemDTO> {

	@Override
	public SaleListItem toEntity(SaleListItemDTO dto) {
		return SaleListItem.builder()
					.id(dto.getId())
					.title(dto.getTitle())
					.category(dto.getCategory())
					.targetPrice(dto.getTargetPrice())
					.mainImage(dto.getMainImage())
					.status(dto.getStatus())
					.createdDate(dto.getCreatedDate())
					.build();
	}

	@Override
	public SaleListItemDTO toDTO(SaleListItem entity) {
		return SaleListItemDTO.builder()
					.id(entity.getId())
					.title(entity.getTitle())
					.category(entity.getCategory())
					.targetPrice(entity.getTargetPrice())
					.mainImage(entity.getMainImage())
					.status(entity.getStatus())
					.createdDate(entity.getCreatedDate())
					.build();
}
}
