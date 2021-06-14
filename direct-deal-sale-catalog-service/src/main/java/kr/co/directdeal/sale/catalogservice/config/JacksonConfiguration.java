package kr.co.directdeal.sale.catalogservice.config;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JacksonConfiguration {

    // public static final DateTimeFormatter FORMATTER = 
    //     DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    //         .withZone(ZoneId.of("Seoul/Asia"));

    // @Bean
    // @Primary
    // public ObjectMapper serializingObjectMapper() {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     JavaTimeModule javaTimeModule = new JavaTimeModule();
    //     javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
    //     javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
    //     objectMapper.registerModule(javaTimeModule);
    //     return objectMapper;
    // }

    // public class InstantSerializer extends JsonSerializer<Instant> {
    //     @Override
    //     public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    //         log.debug("serialize => " + value.toString());
    //         gen.writeString(value.toString());
    //     }
    // }

    // public class InstantDeserializer extends JsonDeserializer<Instant> {
    //     @Override
    //     public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    //         log.debug("deserialize => " + p.getValueAsString());
    //         return Instant.parse(p.getValueAsString());
    //     }
    // }
}