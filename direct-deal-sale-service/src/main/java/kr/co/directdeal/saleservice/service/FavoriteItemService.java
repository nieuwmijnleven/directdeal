package kr.co.directdeal.saleservice.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.FavoriteItem;
import kr.co.directdeal.saleservice.exception.FavoriteItemException;
import kr.co.directdeal.saleservice.service.dto.FavoriteItemDTO;
import kr.co.directdeal.saleservice.service.repository.FavoriteItemRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FavoriteItemService {
    
    private final FavoriteItemRepository favoriteItemRepository;

    private final Mapper<FavoriteItem, FavoriteItemDTO> mapper;

    @Transactional(readOnly = true)
    public List<FavoriteItemDTO> list(FavoriteItemDTO dto) {
        return favoriteItemRepository.findAllByUserId(dto.getUserId())
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    public void save(FavoriteItemDTO dto) {
        FavoriteItem favoriteItem = mapper.toEntity(dto);
        favoriteItem.setCreatedDate(Instant.now());
        favoriteItemRepository.save(favoriteItem);
    }

    @Transactional
    public void delete(FavoriteItemDTO dto) {
        FavoriteItem favoriteItem = favoriteItemRepository.findByUserIdAndItemId(dto.getUserId(), dto.getItemId())
                                        .orElseThrow(() -> FavoriteItemException.builder()
                                                                .messageKey("saleservice.exception.favoriteitemservice.delete.message")
                                                                .messageArgs(new String[]{dto.getUserId(), dto.getItemId()})
                                                                .build());
        favoriteItemRepository.delete(favoriteItem);
    }
}
