package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "kr.co.directdeal.accountservice.service.repository")
@Configuration
public class JPAConfiguration {
    
}
