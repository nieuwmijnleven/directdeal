package kr.co.directdeal.sale.catalogservice.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;

@Component
public class SaleItemMapper implements Mapper<SaleItem, SaleItemDTO> {

	@Override
	public SaleItem toEntity(SaleItemDTO dto) {
		return SaleItem.builder()
					.id(dto.getId())
					.title(dto.getTitle())
					.category(dto.getCategory())
					.targetPrice(dto.getTargetPrice())
					.discountable(dto.isDiscountable())
					.text(dto.getText())
					.images(dto.getImages())
					.status(dto.getStatus())
					.createdDate(dto.getCreatedDate())
					.lastModifiedDate(dto.getLastModifiedDate())
					.build();
	}

	@Override
	public SaleItemDTO toDTO(SaleItem entity) {
		return SaleItemDTO.builder()
					.id(entity.getId())
					.title(entity.getTitle())
					.category(entity.getCategory())
					.targetPrice(entity.getTargetPrice())
					.discountable(entity.isDiscountable())
					.text(entity.getText())
					.images(entity.getImages())
					.status(entity.getStatus())
					.createdDate(entity.getCreatedDate())
					.lastModifiedDate(entity.getLastModifiedDate())
					.build();
	}
}
