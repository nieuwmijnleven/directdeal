package kr.co.directdeal.saleservice.config;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.extensions.kafka.eventhandling.consumer.streamable.StreamableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = {"axon.kafka.consumer.event-processor-mode"}, havingValue = "tracking")
public class AxonConfig {
    @Autowired
    public void configure(final EventProcessingConfigurer configurer, StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource) {
        configurer.registerTrackingEventProcessor("saleitem-processor1", c -> streamableKafkaMessageSource);
        configurer.registerTrackingEventProcessor("saleitem-processor2", c -> streamableKafkaMessageSource);
    }
}
