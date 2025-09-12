package kr.co.directdeal.common.security.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Configuration properties for JWT settings.
 *
 * Properties are bound from application properties/yaml files with prefix "jwt".
 * For example:
 *   jwt.secret=your-secret-key
 *   jwt.accessTokenValidityInSeconds=3600
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "jwt")
@Component
public class JWTProperties {

    /**
     * HTTP header name where JWT token is expected.
     */
    private String header = "Authorization";

    /**
     * Secret key used for signing JWT tokens.
     * This value is Base64 encoded.
     */
    private String secret = "L2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZS9kaXJlY3QtZGVhbC9kaXJlY3QtZGVhbC1hY2NvdW50LXNlcnZpY2UvZGlyZWN0LWRlYWwvZGlyZWN0LWRlYWwtYWNjb3VudC1zZXJ2aWNlL2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZQo=";

    /**
     * Validity period of access token in seconds.
     */
    private long accessTokenValidityInSeconds = 3600;

    /**
     * Validity period of refresh token in seconds.
     */
    private long refreshTokenValidityInSeconds = 2592000;

    /**
     * Get access token validity in milliseconds.
     */
    public long getAccessTokenValidityInMilliseconds() {
        return this.accessTokenValidityInSeconds * 1000;
    }

    /**
     * Get refresh token validity in milliseconds.
     */
    public long getRefreshTokenValidityInMilliseconds() {
        return this.refreshTokenValidityInSeconds * 1000;
    }
}
