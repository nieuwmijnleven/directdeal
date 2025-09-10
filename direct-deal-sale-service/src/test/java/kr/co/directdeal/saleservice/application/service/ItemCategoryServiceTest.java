package kr.co.directdeal.saleservice.application.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.saleservice.domain.object.ItemCategory;
import kr.co.directdeal.saleservice.exception.ItemCategoryException;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import kr.co.directdeal.saleservice.application.service.mapper.ItemCategoryMapper;
import kr.co.directdeal.saleservice.port.outbound.ItemCategoryRepository;

@ExtendWith(MockitoExtension.class)
public class ItemCategoryServiceTest {

    @Mock
    private ItemCategoryRepository itemCategoryRepository;

    private Mapper<ItemCategory, ItemCategoryDTO> mapper = new ItemCategoryMapper();

    private ItemCategoryService itemCategoryService;

    @BeforeEach
    public void init() {
        this.itemCategoryService = new ItemCategoryService(
                                            itemCategoryRepository, 
                                            mapper);
    }
    
    @Test
    public void List_NormalState_ReturnItemCategoryList() {
        //given
        ItemCategory itemCategory = ItemCategory.builder()
                                        .id(1L)
                                        .name("컴퓨터")
                                        .parent(null)
                                        .child(Collections.emptyList())
                                        .build();

        given(itemCategoryRepository.findAllByParentIsNull())
            .willReturn(List.of(itemCategory));

        //when
        List<ItemCategoryDTO> resultList = itemCategoryService.list();

        //then
        verify(itemCategoryRepository).findAllByParentIsNull();

        assertThat(resultList.size(), equalTo(1));
        assertThat(resultList.get(0).getId(), equalTo(itemCategory.getId()));
        assertThat(resultList.get(0).getName(), equalTo(itemCategory.getName()));
        assertThat(resultList.get(0).getParent(), equalTo(itemCategory.getParent()));
        assertThat(resultList.get(0).getChild(), equalTo(itemCategory.getChild()));
    }

    @Test
    public void Insert_HasParentAndSuccessToFindParent_Success() {
        //given
        ItemCategoryDTO parentDTO = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .name("컴퓨터")
                                        .parent(null)
                                        // .child(Collections.singletonList(child));
                                        .build();

        ItemCategoryDTO childDTO = ItemCategoryDTO.builder()
                                        .id(2L)
                                        .name("노트북")
                                        .parent(parentDTO)
                                        .child(Collections.emptyList())
                                        .build();

        parentDTO.setChild(Collections.singletonList(childDTO));
        
        ItemCategory parent = spy(mapper.toEntity(parentDTO));
        given(itemCategoryRepository.findById(childDTO.getParent().getId()))
            .willReturn(Optional.of(parent));

        //when
        itemCategoryService.insert(childDTO);

        //then
        verify(itemCategoryRepository).findById(childDTO.getParent().getId());
        verify(parent).addChildItemCategory(any(ItemCategory.class));
        verify(itemCategoryRepository).save(any(ItemCategory.class));
    }

    @Test
    public void Insert_HasParentAndFailToFindParent_Success() {
        //given
        ItemCategoryDTO parentDTO = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .name("컴퓨터")
                                        .parent(null)
                                        // .child(Collections.singletonList(child));
                                        .build();

        ItemCategoryDTO childDTO = ItemCategoryDTO.builder()
                                        .id(2L)
                                        .name("노트북")
                                        .parent(parentDTO)
                                        .child(Collections.emptyList())
                                        .build();

        parentDTO.setChild(Collections.singletonList(childDTO));
        
        given(itemCategoryRepository.findById(childDTO.getParent().getId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ItemCategoryException.class, () -> {
            itemCategoryService.insert(childDTO);
        });
    }

    @Test
    public void Insert_NoParent_Success() {
        //given
        ItemCategoryDTO childDTO = ItemCategoryDTO.builder()
                                        .id(2L)
                                        .name("노트북")
                                        .parent(null)
                                        .child(Collections.emptyList())
                                        .build();

        //when
        itemCategoryService.insert(childDTO);

        //then
        verify(itemCategoryRepository).save(any(ItemCategory.class));
    }

    @Test
    public void Update_InvalidId_ThrowItemCategoryException() {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                .id(1L)
                                .build();

        given(itemCategoryRepository.findById(dto.getId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ItemCategoryException.class, () -> {
            itemCategoryService.update(dto);
        });
    }

    @Test
    public void Update_ValidId_Success() {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                .id(2L)
                                .name("노트북")
                                .parent(null)
                                .child(Collections.emptyList())
                                .build();
        
        ItemCategory entity = spy(mapper.toEntity(dto));

        given(itemCategoryRepository.findById(dto.getId()))
            .willReturn(Optional.of(entity));

        //when
        itemCategoryService.update(dto);

        //then
        verify(entity).setName(dto.getName());
        verify(entity).setParent(null);
    }

    @Test
    public void Delete_InvalidId_ThrowItemCategoryException() {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                .id(1L)
                                .build();

        given(itemCategoryRepository.findById(dto.getId()))
            .willReturn(Optional.empty());

        //when and then
        assertThrows(ItemCategoryException.class, () -> {
            itemCategoryService.delete(dto);
        });
    }

    @Test
    public void Update_InvalidId_Success() {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                .id(2L)
                                .name("노트북")
                                .parent(null)
                                .child(Collections.emptyList())
                                .build();
        
        ItemCategory entity = spy(mapper.toEntity(dto));

        given(itemCategoryRepository.findById(dto.getId()))
            .willReturn(Optional.of(entity));

        //when
        itemCategoryService.delete(dto);

        //then
        verify(itemCategoryRepository).delete(entity);
    }
}
