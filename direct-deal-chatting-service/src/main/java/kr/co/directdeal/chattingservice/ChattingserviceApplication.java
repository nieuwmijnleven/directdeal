package kr.co.directdeal.chattingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Chatting Service Spring Boot application.
 * <p>
 * This class bootstraps the entire application context.
 * </p>
 */
@SpringBootApplication
public class ChattingserviceApplication {

    /**
     * Starts the Chatting Service application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(ChattingserviceApplication.class, args);
    }
}
