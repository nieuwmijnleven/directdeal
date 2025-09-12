package kr.co.directdeal.saleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the SaleService Spring Boot application.
 *
 * This application handles operations related to item sales,
 * including registration, updates, image handling, and status changes.
 *
 * Uncomment the `exclude = {KafkaAutoConfiguration.class}` line if Kafka auto-configuration should be disabled.
 *
 * @author Cheol Jeon
 */
@SpringBootApplication
public class SaleserviceApplication {

    /**
     * Main method used to launch the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(SaleserviceApplication.class, args);
    }
}
