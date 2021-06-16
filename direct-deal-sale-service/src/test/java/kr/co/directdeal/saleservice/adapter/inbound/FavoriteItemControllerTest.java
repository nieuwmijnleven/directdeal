package kr.co.directdeal.saleservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.saleservice.domain.FavoriteItem;
import kr.co.directdeal.saleservice.service.FavoriteItemService;
import kr.co.directdeal.saleservice.service.dto.FavoriteItemDTO;
import kr.co.directdeal.saleservice.service.mapper.FavoriteItemMapper;
import kr.co.directdeal.saleservice.service.repository.FavoriteItemRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {FavoriteItemController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    FavoriteItemService.class, FavoriteItemMapper.class}))
public class FavoriteItemControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FavoriteItemRepository favoriteItemRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Save_UserIdAndItemId_Success() throws Exception {
        //given
        String payload = 
            objectMapper
                .writeValueAsString(FavoriteItemDTO.builder()
                                        .itemId("80b1616d-c8a8-47f3-80bb-20926444974c")
                                        .build());

        //when and then
        this.mvc.perform(post("/favorite")
                    .content(payload)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(favoriteItemRepository).save(any(FavoriteItem.class));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void List_UserId_Success() throws Exception {
        //given
        FavoriteItemDTO favoriteItemDTO = 
            FavoriteItemDTO.builder()
                .userId("seller@directdeal.co.kr")
                .build();

        given(favoriteItemRepository.findAllByUserId(favoriteItemDTO.getUserId()))
            .willReturn(Collections.singletonList(FavoriteItem.builder()
                                                    .id(1L)
                                                    .userId(favoriteItemDTO.getUserId())
                                                    .itemId("80b1616d-c8a8-47f3-80bb-20926444974c")
                                                    .build()));

        //when and then
        this.mvc.perform(get("/favorite"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].userId", is("seller@directdeal.co.kr")))
                    .andExpect(jsonPath("$[0].itemId", is("80b1616d-c8a8-47f3-80bb-20926444974c")));

        verify(favoriteItemRepository).findAllByUserId(favoriteItemDTO.getUserId());
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Delete_UserIdAndItemId_Success() throws Exception {
        //given
        FavoriteItemDTO favoriteItemDTO = 
            FavoriteItemDTO.builder()
                .userId("seller@directdeal.co.kr")
                .itemId("80b1616d-c8a8-47f3-80bb-20926444974c")
                .build();

        FavoriteItem favoriteItem = FavoriteItem.builder()
                                        .userId(favoriteItemDTO.getUserId())
                                        .itemId(favoriteItemDTO.getItemId())
                                        .createdDate(Instant.now())
                                        .build();

        given(favoriteItemRepository.findByUserIdAndItemId(favoriteItemDTO.getUserId(), favoriteItemDTO.getItemId()))
            .willReturn(Optional.of(favoriteItem));

        //when and then
        this.mvc.perform(delete("/favorite/" + favoriteItemDTO.getItemId()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        verify(favoriteItemRepository).delete(any(FavoriteItem.class));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Delete_UserIdAndInvalidItemId_ThrowSaleItemException() throws Exception {
        //given
        FavoriteItemDTO favoriteItemDTO = 
            FavoriteItemDTO.builder()
                .userId("seller@directdeal.co.kr")
                .itemId("80b1616d-c8a8-47f3-80bb-20926444974c")
                .build();

        given(favoriteItemRepository.findByUserIdAndItemId(favoriteItemDTO.getUserId(), favoriteItemDTO.getItemId()))
            .willReturn(Optional.empty());

        //when and then
        this.mvc.perform(delete("/favorite/" + favoriteItemDTO.getItemId()))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Favorite Item Service Error")))
                    .andExpect(jsonPath("$.message", is("User(seller@directdeal.co.kr) does not have the item(80b1616d-c8a8-47f3-80bb-20926444974c)")));
    }
}
