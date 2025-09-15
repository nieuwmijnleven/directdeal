package kr.co.directdeal.transactionhistoryservice.adapter.inbound;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import kr.co.directdeal.common.security.util.SecurityUtils;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
import kr.co.directdeal.transactionhistoryservice.application.service.TransactionHistoryService;
import kr.co.directdeal.transactionhistoryservice.application.service.dto.TransactionHistoryDTO;
import kr.co.directdeal.transactionhistoryservice.application.service.mapper.TransactionHistoryMapper;
import kr.co.directdeal.transactionhistoryservice.port.outbound.TransactionHistoryRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {TransactionHistoryController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    TransactionHistoryRepository.class, TransactionHistoryMapper.class}))
public class TransactionHistoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void List_NormalState_Success() throws Exception {
        //given
        String userId = SecurityUtils.getCurrentUserLogin();
        TransactionHistoryDTO dto = TransactionHistoryDTO.builder()
                                        .id(1L)
                                        .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                        .sellerId("seller@directdeal.co.kr")
                                        .buyerId("buyer@directdeal.co.kr")
                                        .title("MacBook Pro")
                                        .category("NoteBook")
                                        .targetPrice(10_000_000_000L)
                                        .completionTime(Instant.now().minus(1, ChronoUnit.SECONDS))
                                        .build();

        given(transactionHistoryService.list(userId))
            .willReturn(Collections.singletonList(dto));

        //when and then
        this.mvc.perform(get("/transaction-history"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(dto.getId().intValue())))
                    .andExpect(jsonPath("$[0].itemId", is(dto.getItemId())))
                    .andExpect(jsonPath("$[0].sellerId", is(dto.getSellerId())))
                    .andExpect(jsonPath("$[0].buyerId", is(dto.getBuyerId())))
                    .andExpect(jsonPath("$[0].title", is(dto.getTitle())))
                    .andExpect(jsonPath("$[0].category", is(dto.getCategory())))
                    .andExpect(jsonPath("$[0].targetPrice", is(dto.getTargetPrice())))
                    .andExpect(jsonPath("$[0].completionTime", is(dto.getCompletionTime().toString())));

        verify(transactionHistoryService).list(userId);
    }

    @Test
    @WithMockUser(username = "invalid-user@directdeal.co.kr")
    public void SetBuyer_InvalidUserId_ThrowTransactionHistoryException() throws Exception {
        //given
        String userId = SecurityUtils.getCurrentUserLogin();
        TransactionHistoryDTO dto = TransactionHistoryDTO.builder()
                                        .id(1L)
                                        // .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                        .sellerId("seller@directdeal.co.kr")
                                        .buyerId("buyer@directdeal.co.kr")
                                        // .title("MacBook Pro")
                                        // .category("NoteBook")
                                        // .targetPrice(10_000_000_000L)
                                        // .completionTime(Instant.now().minus(1, ChronoUnit.SECONDS))
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        willThrow(TransactionHistoryException.builder()
                    .messageKey("transactionhistorycontroller.exception.setbuyer.notthesame.message")
                    .messageArgs(new String[]{userId, dto.getSellerId()})
                    .build())   
            .given(transactionHistoryService).setBuyer(any(TransactionHistoryDTO.class));

        //when and then
        this.mvc.perform(put("/transaction-history/setbuyer")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Transaction History Service Error")))
                    .andExpect(jsonPath("$.message", is("userId(invalid-user@directdeal.co.kr) is not the same as sellerId(seller@directdeal.co.kr)")));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void SetBuyer_ValidUserId_Success() throws Exception {
        //given
        TransactionHistoryDTO dto = TransactionHistoryDTO.builder()
                                        .id(1L)
                                        // .itemId("0d49649c-ee95-4a6a-ad92-369bde5ad8b7")
                                        .sellerId("seller@directdeal.co.kr")
                                        .buyerId("buyer@directdeal.co.kr")
                                        // .title("MacBook Pro")
                                        // .category("NoteBook")
                                        // .targetPrice(10_000_000_000L)
                                        // .completionTime(Instant.now().minus(1, ChronoUnit.SECONDS))
                                        .build();

        String payload = objectMapper.writeValueAsString(dto);

        //when and then
        this.mvc.perform(put("/transaction-history/setbuyer")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(payload))
                    .andDo(print())
                    .andExpect(status().isOk());

        verify(transactionHistoryService).setBuyer(any(TransactionHistoryDTO.class));
    }


}
