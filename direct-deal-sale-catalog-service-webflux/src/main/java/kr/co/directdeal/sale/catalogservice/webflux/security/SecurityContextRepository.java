package kr.co.directdeal.sale.catalogservice.webflux.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

	private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest();
        String accessToken = resolveToken(request);
        String requestURI = request.getPath().toString();

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            return ReactiveSecurityContextHolder.getContext()
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        return Mono.empty();
	}

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
           return bearerToken.substring(7);
        }
        return null;
     }
}
