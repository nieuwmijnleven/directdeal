package kr.co.directdeal.sale.catalogservice.config;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
// @AutoConfigureAfter(AxonAutoConfiguration.class)
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

    // @Bean
    // public SimpleCommandBus commandBus(TransactionManager txManager, AxonConfiguration axonConfiguration,
    //                                    DuplicateCommandHandlerResolver duplicateCommandHandlerResolver) {
    //     SimpleCommandBus commandBus =
    //             SimpleCommandBus.builder()
    //                             .transactionManager(txManager)
    //                             .duplicateCommandHandlerResolver(duplicateCommandHandlerResolver)
    //                             .messageMonitor(axonConfiguration.messageMonitor(CommandBus.class, "commandBus"))
    //                             .build();
    //     commandBus.registerHandlerInterceptor(
    //             new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders())
    //     );
    //     return commandBus;
    // }
}
