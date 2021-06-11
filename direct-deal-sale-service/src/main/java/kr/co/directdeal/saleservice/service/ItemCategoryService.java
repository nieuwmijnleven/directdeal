package kr.co.directdeal.saleservice.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.ItemCategory;
import kr.co.directdeal.saleservice.exception.ItemCategoryException;
import kr.co.directdeal.saleservice.service.dto.ItemCategoryDTO;
import kr.co.directdeal.saleservice.service.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemCategoryService {
    
    private final ItemCategoryRepository itemCategoryRepository;

    private final Mapper<ItemCategory, ItemCategoryDTO> mapper;

    @Transactional(readOnly = true)
    public List<ItemCategoryDTO> list() {
        return itemCategoryRepository.findAllByParentIsNull().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    public void insert(ItemCategoryDTO dto) {
        ItemCategory child = mapper.toEntity(dto);
        if (Objects.nonNull(child.getParent())) {
            ItemCategory parent = itemCategoryRepository.findById(child.getParent().getId())
                                        .orElseThrow(() -> ItemCategoryException.builder()
                                                                .messageKey("saleservice.exception.itemcategoryservice.update.message")
                                                                .messageArgs(new String[]{dto.getId().toString()})
                                                                .build());
            parent.addChildItemCategory(child);
        }

        //log.debug("insert : {}", child);
        itemCategoryRepository.save(child);
    }

    @Transactional
    public void update(ItemCategoryDTO dto) {
        ItemCategory itemCategory = itemCategoryRepository.findById(dto.getId())
                                        .orElseThrow(() -> ItemCategoryException.builder()
                                                                .messageKey("saleservice.exception.itemcategoryservice.update.message")
                                                                .messageArgs(new String[]{dto.getId().toString()})
                                                                .build());
        
        itemCategory.setName(dto.getName());
        itemCategory.setParent(Optional.ofNullable(dto.getParent())
                                            .map(ItemCategoryDTO::getId)
                                            .map(id -> ItemCategory.builder()
                                                            .id(id)
                                                            .build())
                                            .orElse(null));
    }

    @Transactional
    public void delete(ItemCategoryDTO dto) {
        ItemCategory itemCategory = itemCategoryRepository.findById(dto.getId())
                                        .orElseThrow(() -> ItemCategoryException.builder()
                                                                .messageKey("saleservice.exception.itemcategoryservice.delete.message")
                                                                .messageArgs(new String[]{dto.getId().toString()})
                                                                .build());
        itemCategoryRepository.delete(itemCategory);
    }
}
