package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.service.BuyHistoryService;
import kr.co.directdeal.transactionhistoryservice.service.dto.BuyHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.service.mapper.BuyHistoryMapper;
import kr.co.directdeal.transactionhistoryservice.service.repository.BuyHistoryRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {BuyHistoryController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    BuyHistoryRepository.class, BuyHistoryMapper.class}))
public class BuyHistoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BuyHistoryService buyHistoryService;

    @Test
    @WithMockUser(username = "buyer@directdeal.co.kr")
    public void List_NormalState_Success() throws Exception {
        //given
        String userId = SecurityUtils.getCurrentUserLogin();
        BuyHistoryDTO dto = BuyHistoryDTO.builder()
                                        .id("1")
                                        .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                        .sellerId("seller@directdeal.co.kr")
                                        .buyerId("buyer@directdeal.co.kr")
                                        .title("MacBook Pro")
                                        .category("NoteBook")
                                        .targetPrice(10_000_000_000L)
                                        .completionTime(Instant.now().minus(1, ChronoUnit.SECONDS))
                                        .build();

        given(buyHistoryService.list(userId))
            .willReturn(Collections.singletonList(dto));

        //when and then
        this.mvc.perform(get("/buy-history"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(dto.getId())))
                    .andExpect(jsonPath("$[0].itemId", is(dto.getItemId())))
                    .andExpect(jsonPath("$[0].sellerId", is(dto.getSellerId())))
                    .andExpect(jsonPath("$[0].buyerId", is(dto.getBuyerId())))
                    .andExpect(jsonPath("$[0].title", is(dto.getTitle())))
                    .andExpect(jsonPath("$[0].category", is(dto.getCategory())))
                    .andExpect(jsonPath("$[0].targetPrice", is(dto.getTargetPrice())))
                    .andExpect(jsonPath("$[0].completionTime", is(dto.getCompletionTime().toString())));

        verify(buyHistoryService).list(userId);
    }

    @Test
    @WithMockUser(username = "invalid-user@directdeal.co.kr")
    public void Delete_InvalidUserId_ThrowBuyHistoryException() throws Exception {
        //given
        String id = "1";
        String userId = SecurityUtils.getCurrentUserLogin();

        willThrow(BuyHistoryException.builder()
                    .messageKey("buyhistoryservice.exception.delete.transaction.notfound.message")
                    .messageArgs(new String[]{ id, userId })
                    .build())   
            .given(buyHistoryService).delete(any(BuyHistoryDTO.class));

        //when and then
        this.mvc.perform(delete("/buy-history/" + id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Buy History Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the transaction(id=1,buyerid=invalid-user@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "buyer@directdeal.co.kr")
    public void Delete_ValidIdAndUserId_Success() throws Exception {
        //given
        String id = "1";

        //when and then
        this.mvc.perform(delete("/buy-history/" + id))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        
        verify(buyHistoryService).delete(any(BuyHistoryDTO.class));
    }
}
