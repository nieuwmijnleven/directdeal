package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// private static final String[] AUTH_WHITELIST = {
    //     "/swagger-resources/**",
    //     "/swagger-ui.html",
    //     "/v2/api-docs",
    //     "/webjars/**"
	// };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().disable()
			.csrf().disable()
			.formLogin().disable()
			.headers().frameOptions().disable();
		
	}

	/*@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers(AUTH_WHITELIST);
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
						"/swagger-resources/**", "/configuration/security",
						"/swagger-ui.html", "/webjars/**”,”/swagger/**");
	}*/
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
