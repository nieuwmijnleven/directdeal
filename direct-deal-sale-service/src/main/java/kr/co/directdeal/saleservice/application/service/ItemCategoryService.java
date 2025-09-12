package kr.co.directdeal.saleservice.application.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.co.directdeal.saleservice.port.inbound.ItemCategoryUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ItemCategory;
import kr.co.directdeal.saleservice.exception.ItemCategoryException;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import kr.co.directdeal.saleservice.port.outbound.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class that implements the ItemCategoryUseCase interface.
 * Handles business logic for item categories including listing, inserting, updating, and deleting categories.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ItemCategoryService implements ItemCategoryUseCase {

    /**
     * Repository interface for item category persistence.
     */
    private final ItemCategoryRepository itemCategoryRepository;

    /**
     * Mapper to convert between ItemCategory entity and ItemCategoryDTO.
     */
    private final Mapper<ItemCategory, ItemCategoryDTO> mapper;

    /**
     * Retrieves a list of root item categories (categories with no parent).
     *
     * @return list of ItemCategoryDTO representing root categories
     */
    @Transactional(readOnly = true)
    @Override
    public List<ItemCategoryDTO> list() {
        return itemCategoryRepository.findAllByParentIsNull().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Inserts a new item category.
     * If the category has a parent, it fetches the parent and associates the new child category.
     *
     * @param dto the ItemCategoryDTO to insert
     * @throws ItemCategoryException if the specified parent category does not exist
     */
    @Transactional
    @Override
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

    /**
     * Updates an existing item category's name and parent.
     *
     * @param dto the ItemCategoryDTO containing updated information
     * @throws ItemCategoryException if the item category to update does not exist
     */
    @Transactional
    @Override
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

    /**
     * Deletes an existing item category.
     *
     * @param dto the ItemCategoryDTO representing the category to delete
     * @throws ItemCategoryException if the item category to delete does not exist
     */
    @Transactional
    @Override
    public void delete(ItemCategoryDTO dto) {
        ItemCategory itemCategory = itemCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> ItemCategoryException.builder()
                        .messageKey("saleservice.exception.itemcategoryservice.delete.message")
                        .messageArgs(new String[]{dto.getId().toString()})
                        .build());
        itemCategoryRepository.delete(itemCategory);
    }
}
