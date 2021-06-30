package kr.co.directdeal.sale.catalogservice.webflux.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

@Configuration
public class WebConfig extends WebFluxConfigurationSupport {
    @Override
    protected void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver());
        super.configureArgumentResolvers(configurer);
    }

    @Override
    protected void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder());
        configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder());
    }

    @Primary
    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new ResponseStatusExceptionHandler();
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .build();
    }

    @Bean
    public Jackson2JsonEncoder jackson2JsonEncoder() {
        return new Jackson2JsonEncoder(objectMapper());
    }

    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder() {
        return new Jackson2JsonDecoder(objectMapper());
    }
}
