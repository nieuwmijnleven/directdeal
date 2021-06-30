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

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider implements InitializingBean {

   private static final String AUTHORITIES_KEY = "authorities";

   private final JWTProperties jwtProperties;

   private Key key;

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }

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

   public String createAccessToken(Authentication authentication) {
      return doCreateToken(authentication, 
               jwtProperties.getAccessTokenValidityInMilliseconds());
   }

   public String createRefreshToken(Authentication authentication) {
      return doCreateToken(authentication, 
               jwtProperties.getRefreshTokenValidityInMilliseconds());
   }

   public String createTokenWithExpireTime(Authentication authentication, long expireTimeInMillis) {
      return doCreateToken(authentication, expireTimeInMillis);
   }

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
