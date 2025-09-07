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

@EnableWebFluxSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()

                .securityContextRepository(securityContextRepository)
                .exceptionHandling()
                    .authenticationEntryPoint((serverWebExchange, exception) -> Mono.fromRunnable(() -> {
                        sendErrorResponse(serverWebExchange, exception, HttpStatus.UNAUTHORIZED);
                    })).accessDeniedHandler((serverWebExchange, exception) -> Mono.fromRunnable(() -> {
                        sendErrorResponse(serverWebExchange, exception, HttpStatus.FORBIDDEN);
                    }))
                    
                .and()
                .authorizeExchange()
                    .pathMatchers("/actuator/health").permitAll()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyExchange().authenticated()

                .and()
                .build();
    }

    private void sendErrorResponse(ServerWebExchange serverWebExchange, Exception exception, HttpStatus status) {
        serverWebExchange.getResponse().setStatusCode(status);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String payload = "{\"error\":\"Authentication Failed\", \"message\":\"" + exception.getMessage() + "\"}";
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        serverWebExchange.getResponse().writeWith(Flux.just(buffer)).subscribe();
    }
}
