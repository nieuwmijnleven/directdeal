package kr.co.directdeal.transactionhistoryservice.config;

import com.mongodb.client.MongoClient;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for Axon Framework components using MongoDB.
 * Provides TokenStore and MongoTemplate beans required for event sourcing.
 *
 * @author Cheol Jeon
 */
@Configuration
public class AxonConfig {
    /**
     * Creates a TokenStore bean using MongoDB as the persistent storage.
     * This bean is used by Axon for storing event processing tokens.
     *
     * @param mongoTemplate the MongoTemplate to interact with MongoDB
     * @param serializer the serializer to serialize events and tokens
     * @return a TokenStore implementation backed by MongoDB
     */
    @Bean
    public TokenStore tokenStore(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(serializer)
                .build();
    }

    /**
     * Creates a MongoTemplate bean configured for Axon Framework usage.
     * This bean is marked as primary to be used preferentially.
     *
     * @param mongoClient the MongoDB client instance
     * @return a MongoTemplate configured for Axon
     */
    @Bean("axonMongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient)
                .build();
    }
}
