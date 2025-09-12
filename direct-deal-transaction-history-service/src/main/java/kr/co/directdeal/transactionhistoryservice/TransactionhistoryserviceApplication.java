package kr.co.directdeal.transactionhistoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Transaction History Service Spring Boot application.
 *
 * @author Cheol Jeon
 */
@SpringBootApplication
public class TransactionhistoryserviceApplication {

    /**
     * Starts the Transaction History Service application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TransactionhistoryserviceApplication.class, args);
    }

}
