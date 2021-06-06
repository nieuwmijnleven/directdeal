package kr.co.directdeal.saleservice.config;

import org.axonframework.extensions.kafka.autoconfig.KafkaAutoConfiguration;
import org.axonframework.extensions.kafka.eventhandling.DefaultKafkaMessageConverter;
import org.axonframework.extensions.kafka.eventhandling.KafkaMessageConverter;
import org.axonframework.serialization.Serializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class KafkaConfig {
    // @Bean
    // public KafkaMessageConverter<String, byte[]> kafkaMessageConverter(
    //         @Qualifier("eventSerializer") Serializer eventSerializer) {
    //     return DefaultKafkaMessageConverter.builder().serializer(eventSerializer).build();
    // }

    // @Bean
    // @ConditionalOnMissingBean
    // public KafkaMessageConverter<String, byte[]> kafkaMessageConverter() {
    //     return DefaultKafkaMessageConverter.builder().serializer(JacksonSerializer.defaultSerializer()).build();
    // }

    // @Bean("axonKafkaProducerFactory")
    // @ConditionalOnMissingBean
    // public ProducerFactory<String, byte[]> kafkaProducerFactory() {
    //     ConfirmationMode confirmationMode = properties.getPublisher().getConfirmationMode();
    //     String transactionIdPrefix = properties.getProducer().getTransactionIdPrefix();

    //     DefaultProducerFactory.Builder<String, byte[]> builder = DefaultProducerFactory.<String, byte[]>builder()
    //             .configuration(properties.buildProducerProperties())
    //             .confirmationMode(confirmationMode);

    //     if (isNonEmptyString(transactionIdPrefix)) {
    //         builder.transactionalIdPrefix(transactionIdPrefix)
    //                .confirmationMode(ConfirmationMode.TRANSACTIONAL);
    //         if (!confirmationMode.isTransactional()) {
    //             logger.warn(
    //                     "The confirmation mode is set to [{}], whilst a transactional id prefix is present. "
    //                             + "The transactional id prefix overwrites the confirmation mode choice to TRANSACTIONAL",
    //                     confirmationMode
    //             );
    //         }
    //     }

    //     return builder.build();
    // }

    // private boolean isNonEmptyString(String s) {
    //     return s != null && !s.equals("");
    // }

    // @ConditionalOnMissingBean
    // @Bean(destroyMethod = "shutDown")
    // @ConditionalOnBean({ProducerFactory.class, KafkaMessageConverter.class})
    // public KafkaPublisher<String, byte[]> kafkaPublisher(ProducerFactory<String, byte[]> axonKafkaProducerFactory,
    //                                                      KafkaMessageConverter<String, byte[]> kafkaMessageConverter,
    //                                                      AxonConfiguration configuration) {
    //     return KafkaPublisher.<String, byte[]>builder()
    //             .producerFactory(axonKafkaProducerFactory)
    //             .messageConverter(kafkaMessageConverter)
    //             .messageMonitor(configuration.messageMonitor(KafkaPublisher.class, "kafkaPublisher"))
    //             .topic(properties.getDefaultTopic())
    //             .build();
    // }

    // @Bean
    // @ConditionalOnMissingBean
    // @ConditionalOnBean({KafkaPublisher.class})
    // public KafkaEventPublisher<String, byte[]> kafkaEventPublisher(KafkaPublisher<String, byte[]> kafkaPublisher,
    //                                                                KafkaProperties kafkaProperties,
    //                                                                EventProcessingConfigurer eventProcessingConfigurer) {
    //     KafkaEventPublisher<String, byte[]> kafkaEventPublisher =
    //             KafkaEventPublisher.<String, byte[]>builder().kafkaPublisher(kafkaPublisher).build();

    //     /*
    //      * Register an invocation error handler which re-throws any exception.
    //      * This will ensure a TrackingEventProcessor to enter the error mode which will retry, and it will ensure the
    //      * SubscribingEventProcessor to bubble the exception to the callee. For more information see
    //      *  https://docs.axoniq.io/reference-guide/configuring-infrastructure-components/event-processing/event-processors#error-handling
    //      */
    //     eventProcessingConfigurer.registerEventHandler(configuration -> kafkaEventPublisher)
    //                              .registerListenerInvocationErrorHandler(
    //                                      DEFAULT_PROCESSING_GROUP, configuration -> PropagatingErrorHandler.instance()
    //                              )
    //                              .assignHandlerTypesMatching(
    //                                      DEFAULT_PROCESSING_GROUP,
    //                                      clazz -> clazz.isAssignableFrom(KafkaEventPublisher.class)
    //                              );

    //     /*
    //      * TODO: Remove the following line after upgrading Axon Framework to release 4.4.3 or higher
    //      *
    //      * Prior to the Axon Framework 4.4.3 release, an instance selector must be assigned as type selectors are
    //      * ignored. After version 4.4.3, this behaviour has changed and therefore upgrading to 4.4.3 or later releases
    //      * will make the following assignment redundant.
    //      *
    //      * For more information see:
    //      *    https://github.com/AxonFramework/extension-kafka/issues/84
    //      *    https://github.com/AxonFramework/AxonFramework/commit/e6249f13e71e70e71c187320bd7ecd1401ac8fbc
    //      */
    //     eventProcessingConfigurer.assignHandlerInstancesMatching(DEFAULT_PROCESSING_GROUP, kafkaEventPublisher::equals);

    //     KafkaProperties.EventProcessorMode processorMode = kafkaProperties.getProducer().getEventProcessorMode();
    //     if (processorMode == KafkaProperties.EventProcessorMode.SUBSCRIBING) {
    //         eventProcessingConfigurer.registerSubscribingEventProcessor(DEFAULT_PROCESSING_GROUP);
    //     } else if (processorMode == KafkaProperties.EventProcessorMode.TRACKING) {
    //         eventProcessingConfigurer.registerTrackingEventProcessor(DEFAULT_PROCESSING_GROUP);
    //     } else {
    //         throw new AxonConfigurationException("Unknown Event Processor Mode [" + processorMode + "] detected");
    //     }

    //     return kafkaEventPublisher;
    // }

    // @Bean("axonKafkaConsumerFactory")
    // @ConditionalOnMissingBean
    // public ConsumerFactory<String, byte[]> kafkaConsumerFactory() {
    //     return new DefaultConsumerFactory<>(properties.buildConsumerProperties());
    // }

    // @ConditionalOnMissingBean
    // @Bean(destroyMethod = "shutdown")
    // public Fetcher<?, ?, ?> kafkaFetcher() {
    //     return AsyncFetcher.builder()
    //                        .pollTimeout(properties.getFetcher().getPollTimeout())
    //                        .build();
    // }

    // @Bean
    // @ConditionalOnMissingBean
    // @ConditionalOnBean({ConsumerFactory.class, KafkaMessageConverter.class, Fetcher.class})
    // @ConditionalOnProperty(value = "axon.kafka.consumer.event-processor-mode", havingValue = "TRACKING")
    // public StreamableKafkaMessageSource<String, byte[]> streamableKafkaMessageSource(
    //         ConsumerFactory<String, byte[]> kafkaConsumerFactory,
    //         Fetcher<String, byte[], KafkaEventMessage> kafkaFetcher,
    //         KafkaMessageConverter<String, byte[]> kafkaMessageConverter
    // ) {
    //     return StreamableKafkaMessageSource.<String, byte[]>builder()
    //             .topics(Collections.singletonList(properties.getDefaultTopic()))
    //             .consumerFactory(kafkaConsumerFactory)
    //             .fetcher(kafkaFetcher)
    //             .messageConverter(kafkaMessageConverter)
    //             .bufferFactory(() -> new SortedKafkaMessageBuffer<>(properties.getFetcher().getBufferSize()))
    //             .build();
    // }
}