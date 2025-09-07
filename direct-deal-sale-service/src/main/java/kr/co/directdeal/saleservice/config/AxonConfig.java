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


@Slf4j
@Configuration
public class AxonConfig {
    @Bean
    public EventStorageEngine storageEngine(MongoClient client, Serializer serializer) {
        return MongoEventStorageEngine.builder()
                    .mongoTemplate(mongoTemplate(client))
                    .eventSerializer(serializer)
                    .snapshotSerializer(serializer)
                    .build();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(org.axonframework.config.Configuration configuration) {
        return new EventCountSnapshotTriggerDefinition(configuration.snapshotter(), 5);
    }

    @Bean("axonMongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder()
                    .mongoDatabase(mongoClient)
                    .build();
    }
}
