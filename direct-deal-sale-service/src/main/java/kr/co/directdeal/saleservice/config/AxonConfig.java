package kr.co.directdeal.saleservice.config;

import com.mongodb.client.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for setting up Axon Framework components with MongoDB.
 *
 * Provides:
 * - EventStorageEngine backed by MongoDB for event sourcing.
 * - SnapshotTriggerDefinition to trigger snapshot every 5 events.
 * - MongoTemplate bean to customize MongoDB interaction.
 *
 * This configuration enables Axon to use MongoDB as the event store.
 *
 * @author Cheol Jeon
 */
@Slf4j
@Configuration
public class AxonConfig {

    /**
     * Creates the EventStorageEngine using MongoDB as the storage backend.
     * Sets the event and snapshot serializers.
     *
     * @param client MongoDB client
     * @param serializer Axon serializer for events and snapshots
     * @return configured EventStorageEngine instance
     */
    @Bean
    public EventStorageEngine storageEngine(MongoClient client, Serializer serializer) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(mongoTemplate(client))
                .eventSerializer(serializer)
                .snapshotSerializer(serializer)
                .build();
    }

    /**
     * Defines a snapshot trigger which triggers snapshot creation every 5 events.
     *
     * @param configuration Axon Configuration instance
     * @return SnapshotTriggerDefinition that triggers after 5 events
     */
    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(org.axonframework.config.Configuration configuration) {
        return new EventCountSnapshotTriggerDefinition(configuration.snapshotter(), 5);
    }

    /**
     * Creates a primary MongoTemplate bean to be used by Axon for MongoDB operations.
     *
     * @param mongoClient MongoDB client
     * @return configured MongoTemplate instance
     */
    @Bean("axonMongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient)
                .build();
    }
}
