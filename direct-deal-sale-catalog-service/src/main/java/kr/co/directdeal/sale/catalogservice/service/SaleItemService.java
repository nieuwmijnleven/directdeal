package kr.co.directdeal.sale.catalogservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleItemRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;

    private final Mapper<SaleItem, SaleItemDTO> mapper;

    @Transactional(readOnly = true)
    public SaleItemDTO findSaleItemById(String id) {
        return saleItemRepository.findById(id)
                    .map(mapper::toDTO)
                    .orElseThrow(() -> SaleItemException.builder()
                                        .messageKey("salecatalogservice.exception.saleitemqueryservice.findsaleitembyid.notfound.message")
                                        .messageArgs(new String[]{id})
                                        .build());                       
    }

    @Transactional(readOnly = true)
    public List<SaleItemDTO> findSaleItemsByOnwerId(String ownerId) {
        return saleItemRepository.findByOwnerIdAndStatus(ownerId, SaleItemStatus.SALE)
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());                     
    }
}
