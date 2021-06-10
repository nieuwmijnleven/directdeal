package kr.co.directdeal.sale.catalogservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.sale.catalogservice.domain.SaleListItem;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListItemDTO;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListItemRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleListQueryService {

    private final SaleListItemRepository saleListItemRepository;

    private final Mapper<SaleListItem, SaleListItemDTO> mapper;

    public List<SaleListItemDTO> list(Pageable pageable) {
        Page<SaleListItem> saleListItems = saleListItemRepository.findAll(pageable);
        return saleListItems.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }
}
