package kr.co.directdeal.common.security.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "jwt")
@Component
public class JWTProperties {

    private String header;

    private String secret;

    private long accessTokenValidityInSeconds;    

    private long refreshTokenValidityInSeconds;    

    public long getAccessTokenValidityInMilliseconds() {
        return this.accessTokenValidityInSeconds * 1000;
    }

    public long getRefreshTokenValidityInMilliseconds() {
        return this.refreshTokenValidityInSeconds * 1000;
    }
}
