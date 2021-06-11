package kr.co.directdeal.sale.catalogservice.adapter.inbound;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SaleListDTO> list(Pageable pageable) {
        log.debug("pageable => " + pageable);
        return saleListService.list(pageable);
    }

    @PutMapping("/{id}/lift-up")
    @ResponseStatus(HttpStatus.OK)
    public void liftUp(@PathVariable("id") @NotBlank String id) {
        saleListService.liftUp(id);
    }
}
