package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// @ComponentScan(basePackageClasses = {SpringSecurityAuditorAware.class})
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class AuditConfiguration {
    
}
