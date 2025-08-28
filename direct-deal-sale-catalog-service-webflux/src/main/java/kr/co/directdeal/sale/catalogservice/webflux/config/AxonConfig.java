package kr.co.directdeal.sale.catalogservice.webflux.config;


import com.mongodb.client.MongoClient;
import org.axonframework.serialization.Serializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import com.thoughtworks.xstream.XStream;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;


@Configuration
public class AxonConfig {
    @Bean
    @Primary
    public XStream mySecuredXStream() {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(new String[]{
            "kr.co.directdeal.**",
            "org.axonframework.**" 
        });
        return xStream;
    }
    
    @Bean
    @Primary
    public Serializer xStreamSerializer(XStream xStream) {
        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

    @Bean
    public TokenStore tokenStore(MongoTemplate mongoTemplate, Serializer serializer) {
        return MongoTokenStore.builder()
                    .mongoTemplate(mongoTemplate)
                    .serializer(serializer)
                    .build();
    }

    @Bean("axonMongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient client) {
        return DefaultMongoTemplate.builder()
                    .mongoDatabase(client)
                    .build();
    }
}
