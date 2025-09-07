package kr.co.directdeal.sale.catalogservice.webflux.config;


import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.xml.XStreamSerializer;

import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.axonframework.common.AxonConfigurationException;
import org.axonframework.common.Registration;
import org.axonframework.common.stream.BlockingStream;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventhandling.TrackingToken;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.kafka.KafkaProperties;
import org.axonframework.extensions.kafka.configuration.KafkaMessageSourceConfigurer;
import org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerSeekUtil;
import org.axonframework.extensions.kafka.eventhandling.consumer.DefaultConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.Fetcher;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.extensions.kafka.eventhandling.consumer.subscribable.SubscribableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Collections;
import java.util.function.Supplier;

@Configuration
@ConditionalOnProperty(
        value = "axon.kafka.consumer.event-processor-mode",
        havingValue = "subscribing"
)
public class SubscribingConfiguration {

    /**
     * To start a SubscribableKafkaMessageSource at the right point in time, 
     * we should add those sources to a KafkaMessageSourceConfigurer.
     */
    @Bean
    public KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer() {
        return new KafkaMessageSourceConfigurer();
    }

    /**
     * The KafkaMessageSourceConfigurer should be added to Axon's Configurer 
     * to ensure it will be called upon start up.
     */
    @Autowired
    public void registerKafkaMessageSourceConfigurer(
            Configurer configurer,
            KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer
    ) {
        configurer.registerModule(kafkaMessageSourceConfigurer);
    }

    /**
     * The autoconfiguration currently does not create a SubscribableKafkaMessageSource bean 
     * because the user is inclined to provide the group-id in all scenarios. 
     * Doing so provides users the option to create several 
     * SubscribingEventProcessor beans belonging to the same group, thus giving Kafka the opportunity to balance the load.
     *
     * Additionally, this subscribable source should be added to the KafkaMessageSourceConfigurer 
     * to ensure it will be started and stopped within the configuration lifecycle.
     */
    @Bean(name="subscribableKafkaMessageSource")
    public SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource(
            KafkaProperties kafkaProperties,
            ConsumerFactory<String, byte[]> consumerFactory,
            Fetcher<String, byte[], EventMessage<?>> fetcher,
            KafkaMessageConverter<String, byte[]> messageConverter,
            KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer
    ) {
        SubscribableKafkaMessageSource<String, byte[]> source =
                SubscribableKafkaMessageSource.<String, byte[]>builder()
                        .topics(Collections.singletonList(kafkaProperties.getDefaultTopic()))
                        .groupId("saleItem")
                        .consumerFactory(consumerFactory)
                        .fetcher(fetcher)
                        .messageConverter(messageConverter)
                        .build();

        // ✅ Supplier 사용
        kafkaMessageSourceConfigurer.configureSubscribableSource(configure -> source);

        return source;
    }

    @Autowired
    public void configureSubscribableKafkaSource(
            EventProcessingConfigurer eventProcessingConfigurer,
            SubscribableKafkaMessageSource<String, byte[]> subscribableKafkaMessageSource
    ) {
        eventProcessingConfigurer.registerSubscribingEventProcessor(
                "saleItem",
                configuration -> subscribableKafkaMessageSource
        );
    }
}