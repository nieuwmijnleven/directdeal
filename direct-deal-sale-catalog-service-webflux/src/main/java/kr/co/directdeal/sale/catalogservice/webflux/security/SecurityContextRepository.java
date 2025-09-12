package kr.co.directdeal.sale.catalogservice.webflux.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * SecurityContextRepository extracts JWT token from the HTTP request headers,
 * validates it, and loads the corresponding Authentication into the reactive security context.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    /**
     * Saving SecurityContext is not supported.
     *
     * @param swe ServerWebExchange
     * @param sc SecurityContext to save
     * @throws UnsupportedOperationException always thrown as this method is not supported
     */
    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Loads the SecurityContext by extracting JWT token from the Authorization header,
     * validating it, and retrieving Authentication.
     *
     * @param swe ServerWebExchange containing the HTTP request
     * @return Mono emitting the SecurityContext if token is valid, or empty if no valid token is found
     */
    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        ServerHttpRequest request = swe.getRequest();
        String accessToken = resolveToken(request);
        String requestURI = request.getPath().toString();

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            log.info("Stored authentication '{}' in Security Context, uri: {}", authentication.getName(), requestURI);
            return ReactiveSecurityContextHolder.getContext()
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        log.info("No valid JWT token found, uri: {}", requestURI);
        return Mono.empty();
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request ServerHttpRequest containing headers
     * @return the JWT token string if present and starts with "Bearer ", otherwise null
     */
    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            return bearerToken.substring(AUTHORIZATION_TOKEN_PREFIX.length());
        }
        return null;
    }
}
