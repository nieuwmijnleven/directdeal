package kr.co.directdeal.saleservice.application.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import kr.co.directdeal.saleservice.port.inbound.FavoriteItemUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.FavoriteItem;
import kr.co.directdeal.saleservice.exception.FavoriteItemException;
import kr.co.directdeal.saleservice.application.service.dto.FavoriteItemDTO;
import kr.co.directdeal.saleservice.port.outbound.FavoriteItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service class that implements the FavoriteItemUseCase interface.
 * Handles business logic for favorite items including listing, saving, and deleting favorite items.
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Service
public class FavoriteItemService implements FavoriteItemUseCase {

    /**
     * Repository interface for favorite item persistence.
     */
    private final FavoriteItemRepository favoriteItemRepository;

    /**
     * Mapper to convert between FavoriteItem entity and FavoriteItemDTO.
     */
    private final Mapper<FavoriteItem, FavoriteItemDTO> mapper;

    /**
     * Retrieves a list of favorite items for a specific user.
     *
     * @param dto the FavoriteItemDTO containing the userId to filter by
     * @return list of FavoriteItemDTO objects belonging to the user
     */
    @Transactional(readOnly = true)
    @Override
    public List<FavoriteItemDTO> list(FavoriteItemDTO dto) {
        return favoriteItemRepository.findAllByUserId(dto.getUserId())
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new favorite item record.
     * Sets the created date to the current timestamp before saving.
     *
     * @param dto the FavoriteItemDTO to save
     */
    @Transactional
    @Override
    public void save(FavoriteItemDTO dto) {
        FavoriteItem favoriteItem = mapper.toEntity(dto);
        favoriteItem.setCreatedDate(Instant.now());
        favoriteItemRepository.save(favoriteItem);
    }

    /**
     * Deletes an existing favorite item based on userId and itemId.
     * Throws FavoriteItemException if the favorite item is not found.
     *
     * @param dto the FavoriteItemDTO containing userId and itemId to identify the record to delete
     * @throws FavoriteItemException if no favorite item matches the given userId and itemId
     */
    @Transactional
    @Override
    public void delete(FavoriteItemDTO dto) {
        FavoriteItem favoriteItem = favoriteItemRepository.findByUserIdAndItemId(dto.getUserId(), dto.getItemId())
                .orElseThrow(() -> FavoriteItemException.builder()
                        .messageKey("saleservice.exception.favoriteitemservice.delete.message")
                        .messageArgs(new String[]{dto.getUserId(), dto.getItemId()})
                        .build());
        favoriteItemRepository.delete(favoriteItem);
    }
}
