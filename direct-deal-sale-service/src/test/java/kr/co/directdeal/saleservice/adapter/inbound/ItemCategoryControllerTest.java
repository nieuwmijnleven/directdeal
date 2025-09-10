package kr.co.directdeal.saleservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

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
import kr.co.directdeal.saleservice.exception.ItemCategoryException;
import kr.co.directdeal.saleservice.application.service.ItemCategoryService;
import kr.co.directdeal.saleservice.application.service.dto.ItemCategoryDTO;
import kr.co.directdeal.saleservice.application.service.mapper.ItemCategoryMapper;
import kr.co.directdeal.saleservice.port.outbound.ItemCategoryRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ItemCategoryController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    ItemCategoryRepository.class, ItemCategoryMapper.class}))
public class ItemCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemCategoryService itemCategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void List_NormalState_Success() throws Exception {
        //given
        ItemCategoryDTO parentDTO = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .name("Appliances")
                                        .parent(null)
                                        .build();

        ItemCategoryDTO childDTO = ItemCategoryDTO.builder()
                                        .id(2L)
                                        .name("Computer")
                                        .parent(ItemCategoryDTO.builder()
                                                    .id(1L)
                                                    .name("Appliances")
                                                    .build())
                                        .child(Collections.emptyList())
                                        .build();
        
        parentDTO.setChild(Collections.singletonList(childDTO));

        given(itemCategoryService.list())
            .willReturn(Collections.singletonList(parentDTO));

        //when and then
        this.mvc.perform(get("/category"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].name", is("Appliances")))
                    .andExpect(jsonPath("$[0].child[0].id", is(2)))
                    .andExpect(jsonPath("$[0].child[0].name", is("Computer")))
                    .andExpect(jsonPath("$[0].child[0].parent.id", is(1)))
                    .andExpect(jsonPath("$[0].child[0].parent.name", is("Appliances")));

        verify(itemCategoryService).list();
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Insert_InvalidParent_ThrowItemCategoryException() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .name("Appliances")
                                        .parent(ItemCategoryDTO.builder()
                                                    .id(2L)
                                                    .name("No Data")
                                                    .build())
                                        .child(Collections.emptyList())
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        willThrow(ItemCategoryException.builder()
                    .messageKey("saleservice.exception.itemcategoryservice.insert.message")
                    .messageArgs(new String[]{ String.valueOf(1L) })
                    .build())
            .given(itemCategoryService).insert(dto);

        //when and then
        this.mvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Item Category Service Error")))
                    .andExpect(jsonPath("$.message", is("Category id(1) is not found")));

        verify(itemCategoryService).insert(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Insert_NormalState_Success() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .name("Appliances")
                                        .parent(ItemCategoryDTO.builder()
                                                    .id(2L)
                                                    .name("No Data")
                                                    .build())
                                        .child(Collections.emptyList())
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        //when and then
        this.mvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemCategoryService).insert(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Update_InvalidId_ThrowItemCategoryException() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .name("Appliances")
                                        .parent(null)
                                        .child(Collections.emptyList())
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        willThrow(ItemCategoryException.builder()
                    .messageKey("saleservice.exception.itemcategoryservice.update.message")
                    .messageArgs(new String[]{ dto.getId().toString() })
                    .build())
            .given(itemCategoryService).update(dto);

        //when and then
        this.mvc.perform(put("/category")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Item Category Service Error")))
                    .andExpect(jsonPath("$.message", is("Category id(1) is not found")));

        verify(itemCategoryService).update(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Update_ValidId_Success() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .name("Appliances")
                                        .parent(null)
                                        .child(Collections.emptyList())
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        //when and then
        this.mvc.perform(put("/category")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemCategoryService).update(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Delete_InvalidId_ThrowItemCategoryException() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .build();

        willThrow(ItemCategoryException.builder()
                    .messageKey("saleservice.exception.itemcategoryservice.delete.message")
                    .messageArgs(new String[]{ dto.getId().toString() })
                    .build())
            .given(itemCategoryService).delete(dto);

        //when and then
        this.mvc.perform(delete("/category/" + dto.getId()))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Item Category Service Error")))
                    .andExpect(jsonPath("$.message", is("Category id(1) is not found")));

        verify(itemCategoryService).delete(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Delete_ValidId_Success() throws Exception {
        //given
        ItemCategoryDTO dto = ItemCategoryDTO.builder()
                                        .id(1L)
                                        .build();

        //when and then
        this.mvc.perform(delete("/category/" + dto.getId()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        verify(itemCategoryService).delete(dto);
    }
}
