package kr.co.directdeal.sale.catalogservice.webflux.config;

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
 * Configuration class for Axon Framework integration.
 * <p>
 * Sets up MongoDB-based TokenStore and MongoTemplate for event sourcing and token management.
 * </p>
 *
 * @author Cheol Jeon
 */
@Configuration
public class AxonConfig {

    /**
     * Creates a MongoTokenStore bean using the given MongoTemplate and Serializer.
     * The token store is responsible for storing event processor tokens in MongoDB.
     *
     * @param mongoTemplate the MongoTemplate to interact with MongoDB
     * @param serializer the Serializer used for serializing token data
     * @return configured TokenStore instance
     */
    @Bean
    public TokenStore tokenStore(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoTokenStore.builder()
                .mongoTemplate(mongoTemplate)
                .serializer(serializer)
                .build();
    }

    /**
     * Creates a primary MongoTemplate bean named "axonMongoTemplate" using the given MongoClient.
     * This template is used by Axon Framework to interact with the MongoDB database.
     *
     * @param mongoClient the MongoClient to connect to MongoDB
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
