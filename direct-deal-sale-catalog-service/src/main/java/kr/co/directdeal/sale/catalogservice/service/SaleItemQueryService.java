package kr.co.directdeal.sale.catalogservice.service;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.dto.SaleItemDTO;
import kr.co.directdeal.sale.catalogservice.query.SaleItem;
import kr.co.directdeal.sale.catalogservice.query.SaleItemQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SaleItemQueryService {

    private final QueryGateway queryGateway;

    public SaleItemDTO findSaleItemById(String id) {
        SaleItemQuery saleItemQuery = SaleItemQuery.builder().id(id).build();
        SaleItem saleItem = queryGateway.query(saleItemQuery, ResponseTypes.instanceOf(SaleItem.class))
                                        .join();
                                        
        return SaleItemDTO.builder()
                    .id(saleItem.getId())
                    .title(saleItem.getTitle())
                    .category(saleItem.getCategory())
                    .targetPrice(saleItem.getTargetPrice())
                    .text(saleItem.getText())
                    .images(saleItem.getImages())
                    .status(saleItem.getStatus())
                    .build();
    }
}
