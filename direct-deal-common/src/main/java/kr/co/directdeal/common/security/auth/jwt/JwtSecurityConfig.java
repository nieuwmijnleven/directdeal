package kr.co.directdeal.common.security.auth.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures the JWT filter in the Spring Security filter chain.
 * This class adds the JwtFilter before the UsernamePasswordAuthenticationFilter
 * to ensure JWT token processing happens early.
 *
 * @author Cheol Jeon
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        // Add JwtFilter before the UsernamePasswordAuthenticationFilter in the chain
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
