package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import kr.co.directdeal.sale.catalogservice.adapter.inbound.response.LiftUpResponse;
import kr.co.directdeal.sale.catalogservice.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.service.SaleListService;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/salelist")
public class SaleListController {

    private final SaleListService saleListService;

    private final LiftUpProperties liftUpProperties;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SaleListDTO> list(Pageable pageable) {
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
