package kr.co.directdeal.sale.catalogservice.webflux.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * JWT 관련 설정 프로퍼티를 관리하는 클래스입니다.
 * <p>
 * application.yml 또는 application.properties의 jwt 접두어 설정과 매핑됩니다.
 * </p>
 *
 * @author Cheol Jeon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ConfigurationProperties(prefix = "jwt")
@Component
public class JWTProperties {

    /**
     * JWT 토큰을 담는 HTTP 헤더명, 기본값은 "Authorization"입니다.
     */
    private String header = "Authorization";

    /**
     * JWT 서명에 사용되는 비밀 키입니다.
     * 기본값은 Base64로 인코딩된 문자열입니다.
     */
    private String secret = "L2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZS9kaXJlY3QtZGVhbC9kaXJlY3QtZGVhbC1hY2NvdW50LXNlcnZpY2UvZGlyZWN0LWRlYWwvZGlyZWN0LWRlYWwtYWNjb3VudC1zZXJ2aWNlL2RpcmVjdC1kZWFsL2RpcmVjdC1kZWFsLWFjY291bnQtc2VydmljZQo=";

    /**
     * 액세스 토큰의 유효 시간(초 단위), 기본값은 3600초(1시간)입니다.
     */
    private long accessTokenValidityInSeconds = 3600;

    /**
     * 리프레시 토큰의 유효 시간(초 단위), 기본값은 2592000초(30일)입니다.
     */
    private long refreshTokenValidityInSeconds = 2592000;

    /**
     * 액세스 토큰 유효 시간을 밀리초 단위로 반환합니다.
     *
     * @return 액세스 토큰 유효 시간 (밀리초)
     */
    public long getAccessTokenValidityInMilliseconds() {
        return this.accessTokenValidityInSeconds * 1000;
    }

    /**
     * 리프레시 토큰 유효 시간을 밀리초 단위로 반환합니다.
     *
     * @return 리프레시 토큰 유효 시간 (밀리초)
     */
    public long getRefreshTokenValidityInMilliseconds() {
        return this.refreshTokenValidityInSeconds * 1000;
    }
}
