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

/**
 * Web configuration class to customize WebFlux behaviors such as
 * argument resolvers and HTTP message codecs.
 * <p>
 * Configures:
 * <ul>
 *   <li>Reactive pageable argument resolver for pagination support.</li>
 *   <li>Jackson JSON encoder/decoder with custom ObjectMapper settings.</li>
 *   <li>WebExceptionHandler to translate exceptions into response status codes.</li>
 * </ul>
 * </p>
 *
 * @author Cheol Jeon
 */
@Configuration
public class WebConfig extends WebFluxConfigurationSupport {

    /**
     * Configure argument resolvers for controller method parameters.
     * Adds support for Reactive pagination via ReactivePageableHandlerMethodArgumentResolver.
     *
     * @param configurer the argument resolver configurer
     */
    @Override
    protected void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ReactivePageableHandlerMethodArgumentResolver());
        super.configureArgumentResolvers(configurer);
    }

    /**
     * Configure HTTP message codecs for request and response body reading/writing.
     * Uses Jackson-based JSON encoder and decoder with customized ObjectMapper.
     *
     * @param configurer the ServerCodecConfigurer to customize
     */
    @Override
    protected void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder());
        configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder());
    }

    /**
     * Provides a WebExceptionHandler bean that maps exceptions to HTTP status codes.
     * Uses the default ResponseStatusExceptionHandler.
     *
     * @return the WebExceptionHandler bean
     */
    @Primary
    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return new ResponseStatusExceptionHandler();
    }

    /**
     * Provides a customized Jackson ObjectMapper bean.
     * Disables writing dates as timestamps, enabling ISO-8601 date formatting.
     *
     * @return the customized ObjectMapper
     */
    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    /**
     * Provides a Jackson2JsonEncoder bean using the customized ObjectMapper.
     *
     * @return the Jackson2JsonEncoder bean
     */
    @Bean
    public Jackson2JsonEncoder jackson2JsonEncoder() {
        return new Jackson2JsonEncoder(objectMapper());
    }

    /**
     * Provides a Jackson2JsonDecoder bean using the customized ObjectMapper.
     *
     * @return the Jackson2JsonDecoder bean
     */
    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder() {
        return new Jackson2JsonDecoder(objectMapper());
    }
}
