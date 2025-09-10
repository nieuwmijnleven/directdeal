package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.sale.catalogservice.webflux.config.WebConfig;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.SaleItemService;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.mapper.SaleItemMapper;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleItemRepository;
import reactor.core.publisher.Mono;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {SaleItemController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    SaleItemRepository.class, SaleItemMapper.class, 
                    WebConfig.class}))
@ActiveProfiles("test")
public class SaleItemControllerTest {

    @MockBean
    private SaleItemService saleItemService;

    @Autowired
    private WebTestClient webClient;

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
            .willReturn(Mono.just(dto));

        //when and then
        webClient.get()
            .uri("/saleitem/" + id)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.id").isEqualTo(dto.getId())
                .jsonPath("$.title").isEqualTo(dto.getTitle())
                .jsonPath("$.category").isEqualTo(dto.getCategory())
                .jsonPath("$.targetPrice").isEqualTo(dto.getTargetPrice())
                .jsonPath("$.status").isEqualTo(dto.getStatus().toString());
                // .jsonPath("$.createdDate").isEqualTo(dto.getCreatedDate().toString())
                // .jsonPath("$.lastModifiedDate").isEqualTo(dto.getLastModifiedDate().toString());

      verify(saleItemService).findSaleItemById(id);
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void GetSaleItem_InvalidId_ThrowSaleItemException() throws Exception {
        //given
        String id = "1";
        willThrow(SaleItemException.builder()
                    .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                    .messageArgs(new String[]{id})
                    .build())
        .given(saleItemService).findSaleItemById(id);

        //when and then
        webClient.get()
            .uri("/saleitem/" + id)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
                .jsonPath("$.error").isEqualTo("Sale Item Service Error")
                .jsonPath("$.message").isEqualTo("Failed to find the sale item(id = 1)");

      verify(saleItemService).findSaleItemById(id);
    }
}
