package kr.co.directdeal.sale.catalogservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import kr.co.directdeal.sale.catalogservice.config.prop.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RedisConfig {
    
    private final RedisProperties redisProperties;

    private final ObjectMapper objectMapper;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.debug("redisProperties => {}", redisProperties);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        //redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        // redisTemplate.setEnableDefaultSerializer(true);
        return redisTemplate;
    }

    // @Bean
    // public RedisSerializer<Object> GenericJackson2JsonRedisSerializerEx() {
    //     ObjectMapper om = new ObjectMapper();
    //     om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    //     om.registerModule(new JavaTimeModule());
    //     // om.registerModule((new SimpleModule())
    //     //     .addSerializer(new NullValueSerializer()));
    //     // om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    //     return new GenericJackson2JsonRedisSerializer(om);
    // }

    // // @Bean
    // private ObjectMapper redisObjectMapper() {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    //     objectMapper.registerModules(new JavaTimeModule(), new Jdk8Module());
    //     return objectMapper;
    // }
}
