package kr.co.directdeal.saleservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.FavoriteItem;
import kr.co.directdeal.saleservice.exception.FavoriteItemException;
import kr.co.directdeal.saleservice.service.dto.FavoriteItemDTO;
import kr.co.directdeal.saleservice.service.mapper.FavoriteItemMapper;
import kr.co.directdeal.saleservice.service.repository.FavoriteItemRepository;

@ExtendWith(MockitoExtension.class)
public class FavoriteItemServiceTest {
    @Mock
    private FavoriteItemRepository favoriteItemRepository;

    private Mapper<FavoriteItem, FavoriteItemDTO> mapper = new FavoriteItemMapper();
    
    private FavoriteItemService favoriteItemService;

    @BeforeEach
    public void init() {
        this.favoriteItemService = new FavoriteItemService(
                                            favoriteItemRepository, 
                                            mapper);
    }

    @Test
    public void List_ValidId_ReturnList() {
        //given
        Instant createdDate = Instant.now();
        FavoriteItemDTO dto = FavoriteItemDTO.builder()
                                    .userId("user@directdeal.co.kr")
                                    .build();

        FavoriteItem entity = FavoriteItem.builder()
                                    .id("1")
                                    .userId(dto.getUserId())
                                    .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                    .createdDate(createdDate)
                                    .build();

        given(favoriteItemRepository.findAllByUserId(dto.getUserId()))
            .willReturn(Collections.singletonList(entity));

        //when
        List<FavoriteItemDTO> resultList = favoriteItemService.list(dto);

        //then
        assertThat(resultList.size(), equalTo(1));
        assertThat(resultList.get(0).getId(), equalTo("1"));
        assertThat(resultList.get(0).getUserId(), equalTo("user@directdeal.co.kr"));
        assertThat(resultList.get(0).getItemId(), equalTo("0d49649c-ee95-4a6a-ad92-369bde5ad8b7"));
        assertThat(resultList.get(0).getCreatedDate(), equalTo(createdDate));
    }

    @Test
    public void List_NotExistId_ReturnEmptyList() {
        //given
        FavoriteItemDTO dto = FavoriteItemDTO.builder()
                                    .userId("user@directdeal.co.kr")
                                    .build();

        given(favoriteItemRepository.findAllByUserId(dto.getUserId()))
            .willReturn(Collections.emptyList());

        //when
        List<FavoriteItemDTO> resultList = favoriteItemService.list(dto);

        //then
        assertThat(resultList.isEmpty(), equalTo(true));
    }

    @Test
    public void Save_NormalState_Success() {
        //given
        FavoriteItemDTO dto = FavoriteItemDTO.builder()
                                    .userId("user@directdeal.co.kr")
                                    .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                    .build();

        //when
        favoriteItemService.save(dto);

        //then
        verify(favoriteItemRepository).save(any(FavoriteItem.class));
    }

    @Test
    public void Delete_ValidId_Success() {
        //given
        FavoriteItemDTO dto = FavoriteItemDTO.builder()
                                    .userId("user@directdeal.co.kr")
                                    .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                    .build();

        FavoriteItem entity = FavoriteItem.builder()
                                    .id("1")
                                    .userId("user@directdeal.co.kr")
                                    .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                    .createdDate(Instant.now())
                                    .build();

        given(favoriteItemRepository.findByUserIdAndItemId(dto.getUserId(), dto.getItemId()))
            .willReturn(Optional.of(entity));

        //when
        favoriteItemService.delete(dto);

        //then
        verify(favoriteItemRepository).delete(entity);
    }

    @Test
    public void Delete_InvalidId_ThrowFavoriteItemException() {
        //given
        FavoriteItemDTO dto = FavoriteItemDTO.builder()
                                    .userId("user@directdeal.co.kr")
                                    .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                    .build();

        given(favoriteItemRepository.findByUserIdAndItemId(dto.getUserId(), dto.getItemId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(FavoriteItemException.class, () -> {
            favoriteItemService.delete(dto);
        });        
    }
}
