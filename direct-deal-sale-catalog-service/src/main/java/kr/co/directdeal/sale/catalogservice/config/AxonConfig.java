package kr.co.directdeal.sale.catalogservice.config;

import org.axonframework.springboot.autoconfig.AxonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AxonAutoConfiguration.class)
public class AxonConfig {
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
