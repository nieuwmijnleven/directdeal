package kr.co.directdeal.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) filter.
 *
 * <p>This configuration allows cross-origin requests to the endpoints under "/api/**".
 * It permits any origin, header, and HTTP method, and supports credentials.</p>
 *
 * <p>Author: Cheol Jeon</p>
 */
@Configuration
public class CORSConfiguration {

    /**
     * Defines a CorsFilter bean to handle CORS requests.
     *
     * @return CorsFilter configured with permissive CORS settings for "/api/**" paths.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }

}
