package kr.co.directdeal.sale.catalogservice.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DirectDealSaleCatalogServiceWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectDealSaleCatalogServiceWebfluxApplication.class, args);
	}

}
