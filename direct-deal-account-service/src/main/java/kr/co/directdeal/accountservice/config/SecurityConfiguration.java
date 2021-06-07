package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.co.directdeal.common.security.auth.jwt.JwtAccessDeniedHandler;
import kr.co.directdeal.common.security.auth.jwt.JwtAuthenticationEntryPoint;
import kr.co.directdeal.common.security.auth.jwt.JwtSecurityConfig;
import kr.co.directdeal.common.security.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = {"kr.co.directdeal.common.security"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final TokenProvider tokenProvider;
    // private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// .cors()
			// .disable()
			
			.csrf()
				.disable()
			
			.formLogin()
				.disable()
			
			.headers()
				.frameOptions()
					.disable()

			// .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

			// .and()
			// .addFilterBefore(filter, beforeFilter)
			// enable h2-console
			// .and()
			// .headers()
			// 	.frameOptions()
			// 	.sameOrigin()

			// 세션을 사용하지 않기 때문에 STATELESS로 설정
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/auth/login").permitAll()
				// .antMatchers(HttpMethod.GET, "/account").hasAnyRole(AuthorityConstants.USER).
				.anyRequest().authenticated()

			.and()
			.apply(new JwtSecurityConfig(tokenProvider));
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
			.antMatchers(HttpMethod.OPTIONS, "/**")
			.antMatchers("/v2/api-docs")
			.antMatchers("/configuration/ui")
			.antMatchers("/webjars/**")
			.antMatchers("/swagger/**")
			.antMatchers("/swagger-resources/**")
			.antMatchers("/swagger-ui.html")
			.antMatchers("/h2-console/**")

			.antMatchers(HttpMethod.POST, "/account");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
