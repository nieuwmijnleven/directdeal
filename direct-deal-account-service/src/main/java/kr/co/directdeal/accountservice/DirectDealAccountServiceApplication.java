package kr.co.directdeal.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Spring Boot Application class for DirectDeal Account Service.
 *
 * <p>This class bootstraps the application and configures
 * the message source and validator beans.</p>
 *
 * @author Cheol Jeon
 */
@SpringBootApplication
public class DirectDealAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DirectDealAccountServiceApplication.class, args);
    }

    /**
     * Configures the MessageSource bean to load messages for internationalization.
     * Uses ReloadableResourceBundleMessageSource to reload messages without restarting.
     *
     * @return the configured MessageSource bean
     */
    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }

    /**
     * Configures the validator bean to support message interpolation with
     * the configured MessageSource for validation error messages.
     *
     * @return the configured LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
