package kr.co.directdeal.sale.catalogservice.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.query.SaleListItem;
import kr.co.directdeal.sale.catalogservice.query.SaleListItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListCommandService {

    private final SaleListItemRepository saleListItemRepository;

    private final SaleListItemDomainService saleListItemDomainService;

    public boolean liftUp(String id) {
        SaleListItem saleListItem = saleListItemRepository.findById(id)
                                        .orElseThrow(IllegalArgumentException::new);

        if (saleListItemDomainService.canLiftUp(saleListItem)) {
            log.debug("liftup saleListItem : {} => ", saleListItem);
            saleListItemDomainService.liftUp(saleListItem);
            saleListItemRepository.save(saleListItem); 
            return true;
        }
        return false;
    }
    
}
