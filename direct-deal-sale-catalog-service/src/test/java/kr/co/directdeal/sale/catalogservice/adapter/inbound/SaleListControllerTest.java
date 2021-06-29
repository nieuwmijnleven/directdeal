package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.sale.catalogservice.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.service.SaleListService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.service.mapper.SaleListMapper;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {SaleListController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class, 
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    SaleListRepository.class, SaleListMapper.class, LiftUpProperties.class}))
public class SaleListControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SaleListService saleListService;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void List_NormalState_Success() throws Exception {
        //given
        SaleListDTO dto = SaleListDTO.builder()
                                .id("1")
                                .title("MacBook Pro")
                                .category("Computer")
                                .targetPrice(10_000_000_000L)
                                .mainImage("/local/repository/1.png")
                                .status(SaleItemStatus.SALE)
                                .createdDate(Instant.now().minus(1, ChronoUnit.DAYS))
                                .build();

        given(saleListService.list(any(Pageable.class)))
            .willReturn(Collections.singletonList(dto));

        //when and then
        this.mvc.perform(get("/salelist"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id", is(dto.getId())))
                    .andExpect(jsonPath("$[0].title", is(dto.getTitle())))
                    .andExpect(jsonPath("$[0].category", is(dto.getCategory())))
                    .andExpect(jsonPath("$[0].targetPrice", is(dto.getTargetPrice())))
                    .andExpect(jsonPath("$[0].mainImage", is("/local/repository/1.png")))
                    .andExpect(jsonPath("$[0].status", is(dto.getStatus().toString())))
                    .andExpect(jsonPath("$[0].createdDate", is(dto.getCreatedDate().toString())));

        verify(saleListService).list(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void LiftUp_InvalidId_ThrowSaleListException() throws Exception {
        //given
        String id = "1";
        willThrow(SaleListException.builder()
                    .messageKey("salecatalogservice.exception.salelistcommandservice.liftup.notfound.message")
                    .messageArgs(new String[]{ id })
                    .build())
            .given(saleListService).liftUp(id);

        //when and then
        this.mvc.perform(put("/salelist/" + id + "/lift-up"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Sale List Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the sale list item(id = 1)")));
        
        verify(saleListService).liftUp(id);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void LiftUp_CanLiftUpIsTrue_Success() throws Exception {
        //given
        String id = "1";
        given(saleListService.liftUp(id))
            .willReturn(true);

        //when and then
        this.mvc.perform(put("/salelist/" + id + "/lift-up"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result", is("SUCCESS")))
                    .andExpect(jsonPath("$.intervalDays", is(1)));
        
        verify(saleListService).liftUp(id);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void LiftUp_CanLiftUpIsFalse_Failure() throws Exception {
        //given
        String id = "1";
        given(saleListService.liftUp(id))
            .willReturn(false);

        //when and then
        this.mvc.perform(put("/salelist/" + id + "/lift-up"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result", is("FAILURE")))
                    .andExpect(jsonPath("$.intervalDays", is(1)));
        
        verify(saleListService).liftUp(id);
    }
}
