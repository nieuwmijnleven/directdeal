package kr.co.directdeal.sale.catalogservice.webflux.config.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Properties class that manages settings related to sale list lift-up (top exposure).
 * <p>
 * Reads the property {@code direct.deal.sale.catalog.liftup.interval.day} from
 * {@code application.yml} or {@code application.properties}.
 * The default value is set to 1 day.
 * </p>
 *
 * @author Cheol Jeon
 */
@Data
@Component
public class LiftUpProperties {

    /**
     * Lift-up interval in days (default: 1 day)
     */
    @Value("${direct.deal.sale.catalog.liftup.interval.day:1}")
    private int liftUpIntervalDays;
}
