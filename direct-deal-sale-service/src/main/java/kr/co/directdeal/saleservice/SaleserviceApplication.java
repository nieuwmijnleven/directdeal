package kr.co.directdeal.saleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;

// @SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
@SpringBootApplication
public class SaleserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleserviceApplication.class, args);
	}
}
