package kr.co.directdeal.saleservice.application.service.mapper;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ItemCategory;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemCategoryMapper implements Mapper<ItemCategory, ItemCategoryDTO> {

	@Override
	public ItemCategory toEntity(ItemCategoryDTO dto) {
		if (Objects.isNull(dto))
			return null;
			
		return ItemCategory.builder()
					.id(dto.getId())
					.name(dto.getName())
					.parent(
						Optional.ofNullable(dto.getParent())
							.map(pDTO -> ItemCategory.builder()
												.id(pDTO.getId())
												.name(pDTO.getName())
												.build())
							.orElse(null))
					.child(dto.getChild().stream()
						.map(this::toEntity)
						.collect(Collectors.toList()))
					.build();
	}

	@Override
	public ItemCategoryDTO toDTO(ItemCategory entity) {
		if (Objects.isNull(entity))
			return null;

		return ItemCategoryDTO.builder()
					.id(entity.getId())
					.name(entity.getName())
					.parent(
						Optional.ofNullable(entity.getParent())
							.map(pEntity -> ItemCategoryDTO.builder()
												.id(pEntity.getId())
												.name(pEntity.getName())
												.build())
							.orElse(null))
					.child(entity.getChild().stream()
						.map(this::toDTO)
						.collect(Collectors.toList()))
					.build();
	}
}
