package kr.co.directdeal.transactionhistoryservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	
    @Bean
	public MongoClient mongoClient() { 
        String connectionString = String.format("mongodb://%s:%s@%s:%s/?authSource=%s", username, password, host, port, authenticationDatabase);
        return MongoClients.create(connectionString);
	}
}
