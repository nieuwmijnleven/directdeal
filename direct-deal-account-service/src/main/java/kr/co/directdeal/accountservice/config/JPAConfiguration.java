package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration class to enable JPA repositories.
 *
 * <p>This configuration scans the package {@code kr.co.directdeal.accountservice.port.outbound}
 * for Spring Data JPA repository interfaces to create their implementations automatically.</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
@EnableJpaRepositories(basePackages = "kr.co.directdeal.accountservice.port.outbound")
@Configuration
public class JPAConfiguration {

}
