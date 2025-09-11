package kr.co.directdeal.sale.catalogservice.webflux.port.inbound;

import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface SaleListUseCase {
    Flux<SaleListDTO> list(Pageable pageable);

    boolean liftUp(String id);
}
