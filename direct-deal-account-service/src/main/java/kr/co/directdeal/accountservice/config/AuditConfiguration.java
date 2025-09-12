package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing.
 *
 * <p>This configuration enables auditing support for JPA entities,
 * automatically populating audit-related fields such as createdBy, lastModifiedBy, etc.,
 * based on the current authenticated user provided by the {@code springSecurityAuditorAware} bean.</p>
 *
 * <p>The {@code springSecurityAuditorAware} bean must implement AuditorAware interface
 * to supply the current auditor (user).</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
@Configuration
// @ComponentScan(basePackageClasses = {SpringSecurityAuditorAware.class})
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class AuditConfiguration {

}
