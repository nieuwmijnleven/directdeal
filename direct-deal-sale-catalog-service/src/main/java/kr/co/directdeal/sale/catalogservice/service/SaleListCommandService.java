package kr.co.directdeal.sale.catalogservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.sale.catalogservice.domain.SaleListItem;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListCommandService {

    private final SaleListItemRepository saleListItemRepository;

    private final SaleListItemDomainService saleListItemDomainService;

    @Transactional
    public boolean liftUp(String id) {
        SaleListItem saleListItem = saleListItemRepository.findById(id)
                                        .orElseThrow(IllegalArgumentException::new);

        if (saleListItemDomainService.canLiftUp(saleListItem)) {
            log.debug("liftup saleListItem : {} => ", saleListItem);
            saleListItemDomainService.liftUp(saleListItem);
            // saleListItemRepository.save(saleListItem); 
            return true;
        }
        return false;
    }
}
