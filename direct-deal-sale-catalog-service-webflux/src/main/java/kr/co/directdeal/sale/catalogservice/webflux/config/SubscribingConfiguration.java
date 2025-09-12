package kr.co.directdeal.sale.catalogservice.webflux.config;

import org.axonframework.config.Configurer;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.extensions.kafka.KafkaProperties;
import org.axonframework.extensions.kafka.configuration.KafkaMessageSourceConfigurer;
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.consumer.ConsumerFactory;
import org.axonframework.extensions.kafka.eventhandling.consumer.Fetcher;
import org.axonframework.extensions.kafka.eventhandling.consumer.subscribable.SubscribableKafkaMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Configuration class for setting up Axon Framework's subscribing Kafka event processor.
 * <p>
 * This configuration is activated only when the property
 * {@code axon.kafka.consumer.event-processor-mode=subscribing} is set.
 * It sets up a {@link SubscribableKafkaMessageSource} to enable Kafka consuming in a subscribing mode.
 * </p>
 *
 * <p>Beans configured include:</p>
 * <ul>
 *     <li>{@link KafkaMessageSourceConfigurer} to manage lifecycle of Kafka message sources.</li>
 *     <li>{@link SubscribableKafkaMessageSource} with specific topic, groupId, and necessary components.</li>
 * </ul>
 *
 * <p>The subscribing event processor is registered with the event processing configuration under the name "saleItem".</p>
 *
 * @author Cheol Jeon
 */
@Configuration
@ConditionalOnProperty(
        value = "axon.kafka.consumer.event-processor-mode",
        havingValue = "subscribing"
)
public class SubscribingConfiguration {

    /**
     * Creates a KafkaMessageSourceConfigurer to manage subscribing Kafka message sources lifecycle.
     *
     * @return the KafkaMessageSourceConfigurer bean
     */
    @Bean
    public KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer() {
        return new KafkaMessageSourceConfigurer();
    }

    /**
     * Registers the KafkaMessageSourceConfigurer with Axon's Configurer to ensure
     * it is called during Axon's lifecycle.
     *
     * @param configurer the Axon Configurer
     * @param kafkaMessageSourceConfigurer the KafkaMessageSourceConfigurer bean
     */
    @Autowired
    public void registerKafkaMessageSourceConfigurer(
            Configurer configurer,
            KafkaMessageSourceConfigurer kafkaMessageSourceConfigurer
    ) {
        configurer.registerModule(kafkaMessageSourceConfigurer);
    }

    /**
     * Creates and configures a SubscribableKafkaMessageSource with the given parameters.
     * The source listens to the topic defined in KafkaProperties, uses the consumer group "saleItem",
     * and uses the provided consumer factory, fetcher, and message converter.
     *
     * The source is also registered with the KafkaMessageSourceConfigurer to manage lifecycle.
     *
     * @param kafkaProperties kafka related properties
     * @param consumerFactory consumer factory for creating Kafka consumers
     * @param fetcher fetcher for Kafka messages
     * @param messageConverter converter for Kafka message payloads
     * @param kafkaMessageSourceConfigurer configurer managing Kafka message sources
     * @return the configured SubscribableKafkaMessageSource
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

        // Register source with the configurer for lifecycle management
        kafkaMessageSourceConfigurer.configureSubscribableSource(configure -> source);

        return source;
    }

    /**
     * Registers the SubscribableKafkaMessageSource as a subscribing event processor
     * named "saleItem" with the Axon EventProcessingConfigurer.
     *
     * @param eventProcessingConfigurer the Axon event processing configurer
     * @param subscribableKafkaMessageSource the Kafka message source to register
     */
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
