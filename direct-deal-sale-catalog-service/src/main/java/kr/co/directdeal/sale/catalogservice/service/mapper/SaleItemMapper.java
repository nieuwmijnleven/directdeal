package kr.co.directdeal.sale.catalogservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.query.SaleItem;

@Component
public class SaleItemMapper implements Mapper<SaleItem, SaleItemDTO> {

	@Override
	public SaleItem toEntity(SaleItemDTO dto) {
		return SaleItem.builder()
					.id(dto.getId())
					.title(dto.getTitle())
					.category(dto.getCategory())
					.targetPrice(dto.getTargetPrice())
					.text(dto.getText())
					.images(dto.getImages())
					.status(dto.getStatus())
					.createdDate(dto.getCreatedDate())
					.build();
	}

	@Override
	public SaleItemDTO toDTO(SaleItem entity) {
		return SaleItemDTO.builder()
					.id(entity.getId())
					.title(entity.getTitle())
					.category(entity.getCategory())
					.targetPrice(entity.getTargetPrice())
					.text(entity.getText())
					.images(entity.getImages())
					.status(entity.getStatus())
					.createdDate(entity.getCreatedDate())
					.build();
	}
}
