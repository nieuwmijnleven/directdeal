package kr.co.directdeal.sale.catalogservice.webflux.service;

import org.springframework.stereotype.Service;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.webflux.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.webflux.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.webflux.service.repository.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;

    private final Mapper<SaleItem, SaleItemDTO> mapper;

    public Mono<SaleItemDTO> findSaleItemById(String id) {
        return saleItemRepository.findById(id)
                    .map(mapper::toDTO)
                    .switchIfEmpty(Mono.error(() -> SaleItemException.builder()
                                                        .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                                                        .messageArgs(new String[]{id})
                                                        .build()));                      
    }

    public Flux<SaleItemDTO> findSaleItemsByOnwerId(String ownerId) {
        return saleItemRepository.findByOwnerIdAndStatus(ownerId, SaleItemStatus.SALE)
                    .map(mapper::toDTO);                
    }
}
