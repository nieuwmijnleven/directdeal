package kr.co.directdeal.sale.catalogservice.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.query.SaleListItem;

@Service
public class SaleListItemDomainService {

    public static final int CAN_LIFT_UP_DATE = 3;

    public boolean canLiftUp(SaleListItem saleListItem) {
        LocalDate startLocalDate = LocalDate.ofInstant(saleListItem.getCreatedDate(), ZoneOffset.UTC);
        LocalDate currentLocalDate = LocalDate.now(ZoneOffset.UTC);
        Period period = Period.between(startLocalDate, currentLocalDate);
        return period.getDays() >= 0;
    }

    public void liftUp(SaleListItem saleListItem) {
        if (!canLiftUp(saleListItem)) {
            throw new IllegalArgumentException();
        } 
        saleListItem.setCreatedDate(Instant.now());   
    }
    
}
