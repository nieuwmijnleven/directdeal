package kr.co.directdeal.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@EnableJpaRepositories(basePackages = "kr.co.directdeal.accountservice.service.repository")
@SpringBootApplication
public class DirectDealAccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DirectDealAccountServiceApplication.class, args);
	}
	
	@Bean
	public MessageSource messageSource() {
	    var messageSource = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:/messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setCacheSeconds(10);
	    return messageSource;
	}
	
	@Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
