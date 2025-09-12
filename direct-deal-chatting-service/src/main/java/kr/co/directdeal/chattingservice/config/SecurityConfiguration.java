package kr.co.directdeal.chattingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.JwtSecurityConfig;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security configuration for the chatting service.
 *
 * This class configures HTTP security, including disabling CSRF and form login,
 * configuring stateless session management, exception handling with JWT handlers,
 * and applying JWT security configuration for token-based authentication.
 */
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = {"kr.co.directdeal.common.security"})
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * Configures the security filter chain.
     *
     * Disables CSRF, form login, and frame options.
     * Configures stateless session management.
     * Sets up exception handling for authentication and access denied.
     * Permits OPTIONS requests and the health actuator endpoint.
     * Requires authentication for all other requests.
     * Applies JWT security configuration for validating tokens.
     *
     * @param http the HttpSecurity to modify
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .cors().disable() // 필요 시 CORS Bean 따로 등록
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // Stateless session management (no HTTP sessions)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )

                // Apply JWT Security Config to handle token validation and filter chain
                .with(new JwtSecurityConfig(tokenProvider), Customizer.withDefaults());

        return http.build();
    }
}
