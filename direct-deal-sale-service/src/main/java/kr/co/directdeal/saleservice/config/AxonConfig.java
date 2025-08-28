package kr.co.directdeal.saleservice.config;

import com.mongodb.client.MongoClient;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.DuplicateCommandHandlerResolver;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.axonframework.serialization.xml.XStreamSerializer;
import java.util.Collection;

@Configuration
public class AxonConfig {
    @Bean
    public SimpleCommandBus commandBus(TransactionManager txManager, AxonConfiguration axonConfiguration,
                                       DuplicateCommandHandlerResolver duplicateCommandHandlerResolver) {
        SimpleCommandBus commandBus =
                SimpleCommandBus.builder()
                                .transactionManager(txManager)
                                .duplicateCommandHandlerResolver(duplicateCommandHandlerResolver)
                                .messageMonitor(axonConfiguration.messageMonitor(CommandBus.class, "commandBus"))
                                .build();
        commandBus.registerHandlerInterceptor(
                new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders())
        );
        return commandBus;
    }

    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
    }
    
    @Bean
    @Primary
    public Serializer xStreamSerializer() {
        XStream xStream = new XStream();
        xstream.addPermission(NoTypePermission.NONE);
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xstream.allowTypeHierarchy(Collection.class);
        xStream.allowTypesByWildcard(new String[]{
            "kr.co.directdeal.common.sale.event.**"
        });

        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }
    
    @Bean
    @Qualifier("messageSerializer")
    public Serializer messageSerializer() {
        return xStreamSerializer();
    }

    @Bean
    @Qualifier("eventSerializer")
    public Serializer eventSerializer() {
        return xStreamSerializer();
    }

    @Bean
    public EventStorageEngine storageEngine(MongoClient client) {
        return MongoEventStorageEngine.builder()
                    .mongoTemplate(mongoTemplate(client))
                    .build();
    }

    @Bean
    public TokenStore tokenStore(MongoClient client, Serializer serializer) {
        return MongoTokenStore.builder()
                    .mongoTemplate(mongoTemplate(client))
                    .serializer(serializer)
                    .build();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(org.axonframework.config.Configuration configuration) {
        return new EventCountSnapshotTriggerDefinition(configuration.snapshotter(), 5);
    }

    @Bean("axonMongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient client) {
        return DefaultMongoTemplate.builder()
                    .mongoDatabase(client)
                    .build();
    }
}
