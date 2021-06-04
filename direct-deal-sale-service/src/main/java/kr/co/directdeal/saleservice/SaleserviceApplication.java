package kr.co.directdeal.saleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;

import kr.co.directdeal.saleservice.custom.CustomMultipartResolver;

@SpringBootApplication
public class SaleserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleserviceApplication.class, args);
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CustomMultipartResolver();
	}
}
