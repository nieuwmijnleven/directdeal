package kr.co.directdeal.sale.catalogservice.webflux.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.directdeal.sale.catalogservice.webflux.config.prop.JWTProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TokenProvider handles creation and validation of JWT tokens.
 * It supports generating access and refresh tokens,
 * parsing authentication from tokens, and validating token integrity.
 *
 * @author Cheol Jeon
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "authorities";

    private final JWTProperties jwtProperties;

    private Key key;

    /**
     * Initializes the signing key after properties are set.
     * The key is decoded from a base64-encoded secret.
     */
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Creates a JWT token with a specific expiration time.
     *
     * @param authentication the current authenticated principal
     * @param expireTime expiration time in milliseconds
     * @return the generated JWT token string
     */
    private String doCreateToken(Authentication authentication, long expireTime) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + expireTime);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Creates an access token using the configured access token validity time.
     *
     * @param authentication the current authenticated principal
     * @return the generated access token string
     */
    public String createAccessToken(Authentication authentication) {
        return doCreateToken(authentication,
                jwtProperties.getAccessTokenValidityInMilliseconds());
    }

    /**
     * Creates a refresh token using the configured refresh token validity time.
     *
     * @param authentication the current authenticated principal
     * @return the generated refresh token string
     */
    public String createRefreshToken(Authentication authentication) {
        return doCreateToken(authentication,
                jwtProperties.getRefreshTokenValidityInMilliseconds());
    }

    /**
     * Creates a JWT token with a custom expiration time.
     *
     * @param authentication the current authenticated principal
     * @param expireTimeInMillis expiration time in milliseconds
     * @return the generated JWT token string
     */
    public String createTokenWithExpireTime(Authentication authentication, long expireTimeInMillis) {
        return doCreateToken(authentication, expireTimeInMillis);
    }

    /**
     * Parses the JWT token to extract Authentication information.
     *
     * @param token the JWT token string
     * @return the Authentication object built from the token claims
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Validates the JWT token for correctness and expiration.
     *
     * @param token the JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }
}
