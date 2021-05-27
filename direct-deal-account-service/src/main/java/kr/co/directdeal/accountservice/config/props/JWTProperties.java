package kr.co.directdeal.accountservice.config.props;

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

    private long tokenValidityInSeconds;    

    public long getTokenValidityInMilliseconds() {
        return this.tokenValidityInSeconds * 1000;
    }
}
