package kr.co.directdeal.accountservice.config;

import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import kr.co.directdeal.common.security.config.props.JWTProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTProperties jwtProperties() {
        JWTProperties props = new JWTProperties();
        props.setSecret("RxpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZS9kaXJlY3QdjfksjdkfjkejktZGVhbC9kaXJlY3QtZGVhbC1hY2NvdW50LXNlcnZpY2UvZGlyZWN0LWRlYWwvZGlyZWN0LWRlYWwtYWNjb3VudC1zZXJ2aWNlL2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZQo=");
        return props;
    }

    @Bean
    public TokenProvider tokenProvider(JWTProperties jwtProperties) {
        return new TokenProvider(jwtProperties);
    }

    @Primary
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookiePath("/");
        return tokenRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CsrfTokenRepository csrfTokenRepository) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/refresh").permitAll()
                        .requestMatchers("/auth/csrf").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> {
                    csrf.csrfTokenRepository(csrfTokenRepository)
                            .requireCsrfProtectionMatcher(
                                    AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/refresh")
                            );
                });
        return http.build();
    }
}
