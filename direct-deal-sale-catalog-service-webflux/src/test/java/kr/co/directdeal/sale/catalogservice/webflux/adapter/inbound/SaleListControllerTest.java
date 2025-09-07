package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import kr.co.directdeal.sale.catalogservice.webflux.config.WebConfig;
import kr.co.directdeal.sale.catalogservice.webflux.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.webflux.service.SaleListService;
import kr.co.directdeal.sale.catalogservice.webflux.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.webflux.service.mapper.SaleListMapper;
import kr.co.directdeal.sale.catalogservice.webflux.service.repository.SaleListRepository;
import reactor.core.publisher.Flux;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {SaleListController.class},
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, 
        classes = {TokenProvider.class, JWTProperties.class,
                    SaleListRepository.class, SaleListMapper.class, 
                    LiftUpProperties.class, WebConfig.class}))
@ActiveProfiles("test")
public class SaleListControllerTest {

    @MockBean
    private SaleListService saleListService;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private LiftUpProperties liftUpProperties;

    @BeforeEach
    public void init() {
        liftUpProperties.setLiftUpIntervalDays(1);
    }

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
            .willReturn(Flux.just(dto));

        //when and then
        webClient.get()
            .uri("/salelist")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$[0].id").isEqualTo(dto.getId())
                .jsonPath("$[0].title").isEqualTo(dto.getTitle())
                .jsonPath("$[0].category").isEqualTo(dto.getCategory())
                .jsonPath("$[0].targetPrice").isEqualTo(dto.getTargetPrice())
                .jsonPath("$[0].mainImage").isEqualTo("/local/repository/1.png")
                .jsonPath("$[0].status").isEqualTo(dto.getStatus().toString());
                // .jsonPath("$[0].createdDate").isEqualTo(dto.getCreatedDate().toString());

        verify(saleListService).list(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "seller@directdeal.co.kr")
    public void LiftUp_InvalidId_ThrowSaleListException() throws Exception {
        //given
        String id = "1";
        willThrow(SaleListException.builder()
                    .messageKey("salecatalogservice.exception.salelistcommandservice.liftup.notfound.message")
                    .messageArgs(new String[]{id})
                    .build())
        .given(saleListService).liftUp(id);

        //when and then
        webClient.mutateWith(SecurityMockServerConfigurers.csrf())
            .put()
            .uri("/salelist/" + id + "/lift-up")
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
                .jsonPath("$.error").isEqualTo("Sale List Service Error")
                .jsonPath("$.message").isEqualTo("Failed to find the sale list item(id = 1)");

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
        webClient.mutateWith(SecurityMockServerConfigurers.csrf())
            .put()
            .uri("/salelist/" + id + "/lift-up")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.result").isEqualTo("SUCCESS")
                .jsonPath("$.intervalDays").isEqualTo(1);
        
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
        webClient.mutateWith(SecurityMockServerConfigurers.csrf())
            .put()
            .uri("/salelist/" + id + "/lift-up")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
                .jsonPath("$.result").isEqualTo("FAILURE")
                .jsonPath("$.intervalDays").isEqualTo(1);
        
        verify(saleListService).liftUp(id);
    }
}
