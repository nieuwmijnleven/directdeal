package kr.co.directdeal.sale.catalogservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.dto.SaleListItemDTO;
import kr.co.directdeal.sale.catalogservice.query.SaleListItem;
import kr.co.directdeal.sale.catalogservice.query.SaleListItemRepository;
import kr.co.directdeal.sale.catalogservice.query.SaleListQuery;
import kr.co.directdeal.sale.catalogservice.service.mapper.SaleListItemMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleListQueryService {

    private final QueryGateway queryGateway;

    private final SaleListItemRepository saleListItemRepository;

    private final SaleListItemMapper saleListItemMapper;

    public SaleListItemDTO findSaleListItemById(String id) {
        SaleListQuery saleListQuery = SaleListQuery.builder().id(id).build();
        SaleListItem saleListItem = queryGateway.query(saleListQuery, ResponseTypes.instanceOf(SaleListItem.class))
                                        .join();
                                        
        return SaleListItemDTO.builder()
                    .id(saleListItem.getId())
                    .title(saleListItem.getTitle())
                    .category(saleListItem.getCategory())
                    .targetPrice(saleListItem.getTargetPrice())
                    .mainImage(saleListItem.getMainImage())
                    .status(saleListItem.getStatus())
                    .createdDate(saleListItem.getCreatedDate())
                    .build();
    }

    public List<SaleListItemDTO> list(Pageable pageable) {
        Page<SaleListItem> saleListItems = saleListItemRepository.findAll(pageable);
        return saleListItems.stream()
                    .map(saleListItemMapper::toDTO)
                    .collect(Collectors.toList());
    }
}
