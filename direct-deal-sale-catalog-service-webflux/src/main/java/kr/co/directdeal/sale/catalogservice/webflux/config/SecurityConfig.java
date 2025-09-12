package kr.co.directdeal.sale.catalogservice.webflux.config;

import kr.co.directdeal.sale.catalogservice.webflux.security.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Security configuration for the reactive web application.
 * <p>
 * Configures security context repository, disables CSRF and CORS, customizes exception handling,
 * and sets authorization rules for various endpoints.
 * </p>
 *
 * @author Cheol Jeon
 */
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    /**
     * Configures the security web filter chain.
     *
     * @param http the ServerHttpSecurity to configure
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .securityContextRepository(securityContextRepository)

                .csrf(csrfSpec -> csrfSpec.disable())
                .cors(corsSpec -> corsSpec.disable())

                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint((exchange, ex) ->
                                Mono.fromRunnable(() -> sendErrorResponse(exchange, ex, HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((exchange, ex) ->
                                Mono.fromRunnable(() -> sendErrorResponse(exchange, ex, HttpStatus.FORBIDDEN)))
                )

                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers("/actuator/health").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated()
                )

                .build();
    }

    /**
     * Sends a JSON error response with the given HTTP status and exception message.
     *
     * @param exchange  the ServerWebExchange to write the response to
     * @param exception the exception that caused the error
     * @param status    the HTTP status to set in the response
     */
    private void sendErrorResponse(ServerWebExchange exchange, Exception exception, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String payload = "{\"error\":\"Authentication Failed\", \"message\":\"" + exception.getMessage() + "\"}";
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().writeWith(Flux.just(buffer)).subscribe();
    }
}
