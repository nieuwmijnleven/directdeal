package kr.co.directdeal.sale.catalogservice.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.domain.SaleList;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListDomainService {

    // @Value("${direct.deal.sale.catalog.liftup.interval.day:1}")
    // private int liftUpIntervalDays;
    private final LiftUpProperties liftUpProperties;

    public boolean canLiftUp(SaleList saleList) {
        LocalDate startLocalDate = LocalDate.ofInstant(saleList.getCreatedDate(), ZoneOffset.UTC);
        LocalDate currentLocalDate = LocalDate.now(ZoneOffset.UTC);
        Period period = Period.between(startLocalDate, currentLocalDate);
        log.debug("canLiftupIntervalDays => {}", liftUpProperties.getLiftUpIntervalDays());
        log.debug("period.getDays() => {}", period.getDays());
        return period.getDays() >= liftUpProperties.getLiftUpIntervalDays();
    }

    public void liftUp(SaleList saleList) {
        if (!canLiftUp(saleList)) {
            throw SaleListException.builder()
                    .messageKey("salecatalogservice.exception.salelistdomainservice.liftup.impossible.message")
                    .messageArgs(new String[]{Integer.toString(liftUpProperties.getLiftUpIntervalDays())})
                    .build();
        } 
        saleList.setCreatedDate(Instant.now());   
    }
    
}
