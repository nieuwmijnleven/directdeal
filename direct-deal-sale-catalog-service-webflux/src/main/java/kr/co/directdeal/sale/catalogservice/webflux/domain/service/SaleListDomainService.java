package kr.co.directdeal.sale.catalogservice.webflux.domain.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import kr.co.directdeal.sale.catalogservice.webflux.config.prop.LiftUpProperties;
import kr.co.directdeal.sale.catalogservice.webflux.domain.object.SaleList;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain service responsible for managing SaleList business logic,
 * particularly for handling "lift-up" operations to promote sale items.
 * <p>
 * Uses {@link LiftUpProperties} to enforce lift-up interval restrictions.
 * </p>
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SaleListDomainService {

    private final LiftUpProperties liftUpProperties;

    /**
     * Determines if the given {@link SaleList} can be lifted up based on the configured interval.
     *
     * @param saleList the sale list item to check
     * @return true if the sale list can be lifted up, false otherwise
     */
    public boolean canLiftUp(SaleList saleList) {
        LocalDate startLocalDate = LocalDate.ofInstant(saleList.getCreatedDate(), ZoneOffset.UTC);
        LocalDate currentLocalDate = LocalDate.now(ZoneOffset.UTC);
        Period period = Period.between(startLocalDate, currentLocalDate);

        log.debug("canLiftupIntervalDays => {}", liftUpProperties.getLiftUpIntervalDays());
        log.debug("period.getDays() => {}", period.getDays());

        return period.getDays() >= liftUpProperties.getLiftUpIntervalDays();
    }

    /**
     * Performs the lift-up operation by updating the created date to the current time,
     * if the sale list item is eligible.
     *
     * @param saleList the sale list item to lift up
     * @throws SaleListException if the lift-up is not allowed due to interval restrictions
     */
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
