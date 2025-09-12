package kr.co.directdeal.sale.catalogservice.webflux.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * Configuration class for setting up Reactive MongoDB connection.
 * <p>
 * Reads MongoDB connection properties from application configuration and
 * configures reactive MongoClient and ReactiveMongoTemplate beans.
 * </p>
 *
 * @author Cheol Jeon
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authenticationDatabase;

    @Value("${spring.data.mongodb.database}")
    private String database;

    /**
     * Creates a reactive MongoClient bean using the configured connection properties.
     *
     * @return configured MongoClient instance for reactive MongoDB operations
     */
    @Bean
    public MongoClient mongoClient() {
        String connectionString = String.format(
                "mongodb://%s:%s@%s:%s/?authSource=%s",
                username, password, host, port, authenticationDatabase
        );
        return MongoClients.create(connectionString);
    }

    /**
     * Creates a ReactiveMongoTemplate bean using the reactive MongoClient and database name.
     *
     * @param mongoClient the reactive MongoClient to connect to MongoDB
     * @return configured ReactiveMongoTemplate instance
     */
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, database);
    }
}
