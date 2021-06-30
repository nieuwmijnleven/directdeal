package kr.co.directdeal.sale.catalogservice.webflux.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.webflux.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.webflux.service.repository.SaleListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListService {

    private final SaleListRepository saleListRepository;

    private final Mapper<SaleList, SaleListDTO> mapper;

    private final SaleListDomainService saleListDomainService;

    // @Transactional(readOnly = true)
    public Flux<SaleListDTO> list(Pageable pageable) {
        return saleListRepository
                    .findAllByStatus(pageable, SaleItemStatus.SALE)
                    .map(mapper::toDTO);
    }

    // @Transactional
    public boolean liftUp(String id) {
        Mono<SaleList> saleListMono = 
            saleListRepository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> SaleListException.builder()
                                                    .messageKey("salecatalogservice.exception.salelistcommandservice.liftup.notfound.message")
                                                    .messageArgs(new String[]{id})
                                                    .build()));                                
        SaleList saleList = saleListMono.block();
        if (saleListDomainService.canLiftUp(saleList)) {
            log.debug("liftup saleList : {} => ", saleList);
            saleListDomainService.liftUp(saleList);
            saleListRepository
                .save(saleList)
                .subscribe(result -> log.info("saleList => {}", result));
            return true;
        }
        return false;
    }
}
