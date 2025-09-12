package kr.co.directdeal.saleservice.config;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.JwtSecurityConfig;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = {"kr.co.directdeal.common.security"})
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as JWT is used for security
                .csrf(csrf -> csrf.disable())
                // Disable form login since JWT is used
                .formLogin(form -> form.disable())
                // Allow frames for H2 console or similar if needed (disable frame options)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Exception handling with custom entry point and access denied handler
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                // Stateless session management for JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/image/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                // Apply your custom JWT Security Config
                .with(new JwtSecurityConfig(tokenProvider), Customizer.withDefaults());

        return http.build();
    }
}
