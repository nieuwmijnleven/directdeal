package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.sale.catalogservice.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.service.mapper.SaleItemMapper;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleItemRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {SaleItemController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    JwtAuthenticationEntryPoint.class, JwtAccessDeniedHandler.class,
                    SaleItemRepository.class, SaleItemMapper.class}))
public class SaleItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SaleItemService saleItemService;

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetSaleItem_ValidId_Success() throws Exception {
        //given
        String id = "1";
        SaleItemDTO dto = SaleItemDTO.builder()
                                .id(id)
                                .title("MacBook Pro")
                                .category("Computer")
                                .targetPrice(10_000_000_000L)
                                .text("This is a used MacBook Pro")
                                .images(List.of("/local/repository/1.png", "/local/repository/2.png"))
                                .status(SaleItemStatus.SALE)
                                .createdDate(Instant.now().minus(1, ChronoUnit.DAYS))
                                .lastModifiedDate(Instant.now())
                                .build();

        given(saleItemService.findSaleItemById(id))
            .willReturn(dto);

        //when and then
        this.mvc.perform(get("/saleitem/" + id))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(dto.getId())))
                    .andExpect(jsonPath("$.title", is(dto.getTitle())))
                    .andExpect(jsonPath("$.category", is(dto.getCategory())))
                    .andExpect(jsonPath("$.targetPrice", is(dto.getTargetPrice())))
                    .andExpect(jsonPath("$.status", is(dto.getStatus().toString())))
                    .andExpect(jsonPath("$.createdDate", is(dto.getCreatedDate().toString())))
                    .andExpect(jsonPath("$.lastModifiedDate", is(dto.getLastModifiedDate().toString())));

      verify(saleItemService).findSaleItemById(id);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetSaleItem_InvalidId_ThrowSaleItemException() throws Exception {
        //given
        String id = "1";
        given(saleItemService.findSaleItemById(id))
            .willThrow(SaleItemException.builder()
                            .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                            .messageArgs(new String[]{id})
                            .build());

        //when and then
        this.mvc.perform(get("/saleitem/" + id))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Sale Item Service Error")))
                    .andExpect(jsonPath("$.message", is("Failed to find the sale item(id = 1)")));

      verify(saleItemService).findSaleItemById(id);
    }
}
