package kr.co.directdeal.sale.catalogservice.webflux.config.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LiftUpProperties {
    @Value("${direct.deal.sale.catalog.liftup.interval.day:1}")
    private int liftUpIntervalDays;
}
