package kr.co.directdeal.saleservice.application.service.mapper;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ItemCategory;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * Mapper class to convert between ItemCategory entity and ItemCategoryDTO.
 * Supports recursive conversion of parent and child categories.
 *
 * @author Cheol Jeon
 */
@Slf4j
@Component
public class ItemCategoryMapper implements Mapper<ItemCategory, ItemCategoryDTO> {

    /**
     * Converts ItemCategoryDTO to ItemCategory entity.
     * Recursively maps parent and child categories.
     *
     * @param dto the ItemCategoryDTO to convert
     * @return the corresponding ItemCategory entity, or null if input is null
     */
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

    /**
     * Converts ItemCategory entity to ItemCategoryDTO.
     * Recursively maps parent and child categories.
     *
     * @param entity the ItemCategory entity to convert
     * @return the corresponding ItemCategoryDTO, or null if input is null
     */
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
