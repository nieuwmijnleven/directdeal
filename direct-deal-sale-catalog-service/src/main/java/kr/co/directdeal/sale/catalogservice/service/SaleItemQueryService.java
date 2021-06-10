package kr.co.directdeal.sale.catalogservice.service;

import org.springframework.stereotype.Service;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleItem;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleItemRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleItemQueryService {

    private final SaleItemRepository saleItemRepository;

    private final Mapper<SaleItem, SaleItemDTO> mapper;

    public SaleItemDTO findSaleItemById(String id) {
        return saleItemRepository.findById(id)
                    .map(mapper::toDTO)
                    .orElseThrow(IllegalArgumentException::new);                       
    }
}
