package kr.co.directdeal.common.security.auth.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends GenericFilterBean {

   public static final String AUTHORIZATION_HEADER = "Authorization";

   private TokenProvider tokenProvider;

   public JwtFilter(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      String accessToken = resolveToken(httpServletRequest);
      String requestURI = httpServletRequest.getRequestURI();

      if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
         Authentication authentication = tokenProvider.getAuthentication(accessToken);
         SecurityContextHolder.getContext().setAuthentication(authentication);
         log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
      } else {
         log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;
   }
}
