package kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.webflux.adapter.inbound.response.LiftUpResponse;
import kr.co.directdeal.sale.catalogservice.webflux.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.SaleListService;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/salelist")
public class SaleListController {

    private final SaleListService saleListService;

    private final LiftUpProperties liftUpProperties;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<SaleListDTO> list(Pageable pageable) {
        log.debug("pageable => " + pageable);
        return saleListService.list(pageable);
    }

    @PutMapping("/{id}/lift-up")
    public ResponseEntity<?> liftUp(@PathVariable("id") @NotBlank String id) {
        boolean liftUpResult = saleListService.liftUp(id);
        return ResponseEntity.ok(LiftUpResponse.builder()
                                    .result(liftUpResult ?  LiftUpResponse.ResultConstants.SUCCESS : LiftUpResponse.ResultConstants.FAILURE)
                                    .intervalDays(liftUpProperties.getLiftUpIntervalDays())
                                    .build());
    }
}
