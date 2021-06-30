package kr.co.directdeal.sale.catalogservice.webflux.service.mapper;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.webflux.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.service.dto.SaleListDTO;

@Component
public class SaleListMapper implements Mapper<SaleList, SaleListDTO> {

	@Override
	public SaleList toEntity(SaleListDTO dto) {
		return SaleList.builder()
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
	public SaleListDTO toDTO(SaleList entity) {
		return SaleListDTO.builder()
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
