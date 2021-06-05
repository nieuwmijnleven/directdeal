package kr.co.directdeal.saleservice.config;

import com.mongodb.client.MongoClient;

import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
<<<<<<< HEAD
// @AutoConfigureAfter(AxonAutoConfiguration.class)
=======
//@AutoConfigureBefore({AxonAutoConfiguration.class, AxonServerAutoConfiguration.class})
>>>>>>> 038316a... temporary commit
public class AxonConfig {
    /*@Bean
    public SimpleCommandBus commandBus(TransactionManager txManager, AxonConfiguration axonConfiguration,
                                       DuplicateCommandHandlerResolver duplicateCommandHandlerResolver) {
        SimpleCommandBus commandBus =
                SimpleCommandBus.builder()
                                .transactionManager(txManager)
                                // .duplicateCommandHandlerResolver(duplicateCommandHandlerResolver)
                                .messageMonitor(axonConfiguration.messageMonitor(CommandBus.class, "commandBus"))
                                .build();
        commandBus.registerHandlerInterceptor(
                new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders())
        );
        return commandBus;
    }*/

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore2"))
                .build();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoClient client) {
        return MongoEventStorageEngine.builder()
                    .mongoTemplate(DefaultMongoTemplate.builder()
                                        .mongoDatabase(client)
                                        .build())
                    // .eventSerializer(eventSerializer)
                    .build();
    }

    @Bean
    public TokenStore tokenStore(MongoClient client, Serializer serializer) {
        return MongoTokenStore.builder()
                    .mongoTemplate(
                        DefaultMongoTemplate.builder()
                            .mongoDatabase(client)
                            .build()
                    )
                    .serializer(serializer)
                    .build();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(org.axonframework.config.Configuration configuration) {
        return new EventCountSnapshotTriggerDefinition(configuration.snapshotter(), 5);
    }
}
