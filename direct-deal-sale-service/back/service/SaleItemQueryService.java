package kr.co.directdeal.saleservice.service;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import kr.co.directdeal.saleservice.dto.SaleItemDTO;
import kr.co.directdeal.saleservice.query.SaleItem;
import kr.co.directdeal.saleservice.query.SaleItemQuery;
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
                    .imagePath(saleItem.getImagePath())
                    .status(saleItem.getStatus())
                    .build();
    }
}
