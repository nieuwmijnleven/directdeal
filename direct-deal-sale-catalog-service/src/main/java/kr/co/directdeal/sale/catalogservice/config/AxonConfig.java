package kr.co.directdeal.sale.catalogservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    // @Autowired
    // public EventProcessingConfigurer  streamableKafkaMessageSource(
    //     EventProcessingConfigurer configurer,
    //     StreamableKafkaMessageSource streamableKafkaMessageSource)
    // {
    //     return configurer.registerTrackingEventProcessor("SaleItem", messageSource -> streamableKafkaMessageSource);
    // }        


    // @Bean
    // public void configure(EventProcessingConfigurer config) {
    //     // config.usingSubscribingEventProcessors();
    //     config.usingTrackingEventProcessors();
    // }
}
