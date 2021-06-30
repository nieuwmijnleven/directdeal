package kr.co.directdeal.sale.catalogservice.webflux.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class MongoConfig /*extends AbstractMongoConfiguration*/ {

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
	
    @Bean
	public MongoClient mongoClients() { 
        String connectionString = String.format("mongodb://%s:%s@%s:%s/?authSource=%s", username, password, host, port, authenticationDatabase);
        return MongoClients.create(connectionString);
	}

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClients(), database);
    }
}