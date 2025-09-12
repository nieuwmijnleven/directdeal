package kr.co.directdeal.sale.catalogservice.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * Main class for the Direct Deal Sale Catalog Service WebFlux application.
 * This class bootstraps the Spring Boot application.
 * Security auto-configuration is excluded.
 *
 * @author Cheol Jeon
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DirectDealSaleCatalogServiceWebfluxApplication {

    /**
     * Entry point of the application.
     *
     * @param args application startup arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(DirectDealSaleCatalogServiceWebfluxApplication.class, args);
    }

}
