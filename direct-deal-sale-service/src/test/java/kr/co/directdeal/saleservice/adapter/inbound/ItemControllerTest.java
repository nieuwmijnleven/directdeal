package kr.co.directdeal.saleservice.adapter.inbound;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import kr.co.directdeal.saleservice.application.service.ItemService;
import kr.co.directdeal.saleservice.application.service.dto.ItemDTO;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ItemController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class}))
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Register_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .title("my first item")
                            .category("gems")
                            .targetPrice(10_000_000_000L)
                            .discountable(true)
                            .text("my first item is gems and you can buy it now.")
                            .images(List.of("/local/repository/11.png", "/local/repository/12.png"))
                            .build();

        String payload = objectMapper.writeValueAsString(dto);

        //when and then
        this.mvc.perform(post("/item")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemService).register(any(ItemDTO.class));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Update_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .id("1")
                            .title("my first item")
                            .category("gems")
                            .targetPrice(10_000_000_000L)
                            .discountable(true)
                            .text("my first item is gems and you can buy it now.")
                            .images(List.of("/local/repository/11.png", "/local/repository/12.png"))
                            .build();

        String payload = objectMapper.writeValueAsString(dto);

        //when and then
        this.mvc.perform(put("/item")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemService).update(dto);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Delete_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .id("1")
                            .build();

        //when and then
        this.mvc.perform(delete("/item/" + dto.getId()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        verify(itemService).delete(dto.getId());
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Sale_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .id("1")
                            .build();

        //when and then
        this.mvc.perform(put("/item/" + dto.getId() + "/sale"))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemService).sale(dto.getId());
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Pause_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .id("1")
                            .build();

        //when and then
        this.mvc.perform(put("/item/" + dto.getId() + "/pause"))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemService).pause(dto.getId());
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void Complete_NormalState_Success() throws Exception {
        //given
        ItemDTO dto = ItemDTO.builder()
                            .id("1")
                            .build();

        //when and then
        this.mvc.perform(put("/item/" + dto.getId() + "/complete"))
                    .andDo(print())
                    .andExpect(status().isCreated());

        verify(itemService).complete(dto.getId());
    }
}
