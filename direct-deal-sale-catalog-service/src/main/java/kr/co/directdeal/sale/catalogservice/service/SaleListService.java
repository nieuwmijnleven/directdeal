package kr.co.directdeal.sale.catalogservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.directdeal.common.mapper.Mapper;
import kr.co.directdeal.common.sale.constant.SaleItemStatus;
import kr.co.directdeal.sale.catalogservice.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import kr.co.directdeal.sale.catalogservice.service.dto.SaleListDTO;
import kr.co.directdeal.sale.catalogservice.service.repository.SaleListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListService {

    private final SaleListRepository saleListRepository;

    private final Mapper<SaleList, SaleListDTO> mapper;

    private final SaleListDomainService saleListDomainService;

    @Transactional(readOnly = true)
    public List<SaleListDTO> list(Pageable pageable) {
        Page<SaleList> saleLists = saleListRepository.findAllByStatus(pageable, SaleItemStatus.SALE);
        return saleLists.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Transactional
    public boolean liftUp(String id) {
        SaleList saleList = 
            saleListRepository
                .findById(id)
                .orElseThrow(() -> SaleListException.builder()
                                        .messageKey("salecatalogservice.exception.salelistcommandservice.liftup.notfound.message")
                                        .messageArgs(new String[]{id})
                                        .build());                                

        if (saleListDomainService.canLiftUp(saleList)) {
            log.debug("liftup saleList : {} => ", saleList);
            saleListDomainService.liftUp(saleList);
            saleListRepository.save(saleList); 
            return true;
        }
        return false;
    }
}
