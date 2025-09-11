package kr.co.directdeal.sale.catalogservice.webflux.application.service;

import kr.co.directdeal.sale.catalogservice.webflux.port.inbound.SaleItemUseCase;
import org.springframework.stereotype.Service;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleItem;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.webflux.application.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.webflux.port.outbound.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SaleItemService implements SaleItemUseCase {

    private final SaleItemRepository saleItemRepository;

    private final Mapper<SaleItem, SaleItemDTO> mapper;

    @Override
    public Mono<SaleItemDTO> findSaleItemById(String id) {
        return saleItemRepository.findById(id)
                    .map(mapper::toDTO)
                    .switchIfEmpty(Mono.error(() -> SaleItemException.builder()
                                                        .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                                                        .messageArgs(new String[]{id})
                                                        .build()));                      
    }

    @Override
    public Flux<SaleItemDTO> findSaleItemsByOnwerId(String ownerId) {
        return saleItemRepository.findByOwnerIdAndStatus(ownerId, SaleItemStatus.SALE)
                    .map(mapper::toDTO);                
    }
}
