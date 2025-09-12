package kr.co.directdeal.sale.catalogservice.webflux.application.service;

import kr.co.directdeal.sale.catalogservice.webflux.domain.service.SaleListDomainService;
import kr.co.directdeal.sale.catalogservice.webflux.port.inbound.SaleListUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service implementation for managing sale lists.
 * <p>
 * Provides methods to list sale items and to perform lift-up operations,
 * applying business logic through the domain service.
 * </p>
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListService implements SaleListUseCase {

    private final SaleListRepository saleListRepository;

    private final Mapper<SaleList, SaleListDTO> mapper;

    private final SaleListDomainService saleListDomainService;

    /**
     * Retrieves a paginated list of sale items with status SALE.
     *
     * @param pageable pagination and sorting information
     * @return a Flux stream of SaleListDTO objects
     */
    @Override
    public Flux<SaleListDTO> list(Pageable pageable) {
        return saleListRepository
                .findAllByStatus(pageable, SaleItemStatus.SALE)
                .map(mapper::toDTO);
    }

    /**
     * Attempts to lift up (promote) the sale list item with the given ID.
     * <p>
     * This operation checks whether the item is eligible for lift-up through the domain service.
     * If eligible, it updates the item accordingly and persists the change.
     * </p>
     *
     * @param id the ID of the sale list item to lift up
     * @return true if the lift-up was successful; false otherwise
     * @throws SaleListException if no sale list item with the given ID is found
     */
    @Override
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
