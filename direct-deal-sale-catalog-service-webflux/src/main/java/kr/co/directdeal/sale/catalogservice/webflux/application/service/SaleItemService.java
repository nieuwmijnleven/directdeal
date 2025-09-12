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

/**
 * Service implementation for managing Sale Items.
 * <p>
 * Provides methods to retrieve sale item details by item ID and owner ID.
 * Handles conversion between domain objects and DTOs.
 * </p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Service
public class SaleItemService implements SaleItemUseCase {

    private final SaleItemRepository saleItemRepository;

    private final Mapper<SaleItem, SaleItemDTO> mapper;

    /**
     * Find a sale item by its unique identifier.
     *
     * @param id the unique ID of the sale item
     * @return a Mono emitting the SaleItemDTO if found; otherwise an error Mono
     */
    @Override
    public Mono<SaleItemDTO> findSaleItemById(String id) {
        return saleItemRepository.findById(id)
                .map(mapper::toDTO)
                .switchIfEmpty(Mono.error(() -> SaleItemException.builder()
                        .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                        .messageArgs(new String[]{id})
                        .build()));
    }

    /**
     * Find all sale items belonging to a specific owner that are currently on sale.
     *
     * @param ownerId the ID of the owner (seller)
     * @return a Flux emitting SaleItemDTOs of items on sale by the owner
     */
    @Override
    public Flux<SaleItemDTO> findSaleItemsByOnwerId(String ownerId) {
        return saleItemRepository.findByOwnerIdAndStatus(ownerId, SaleItemStatus.SALE)
                .map(mapper::toDTO);
    }
}
