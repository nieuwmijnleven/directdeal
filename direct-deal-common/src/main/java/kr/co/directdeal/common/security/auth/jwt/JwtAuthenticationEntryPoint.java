package kr.co.directdeal.common.security.auth.jwt;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;

/**
 * Handles AuthenticationException by delegating to a Spring MVC HandlerExceptionResolver.
 * This is used as an entry point for unauthorized requests in JWT authentication.
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        handlerExceptionResolver.resolveException(request, response, null, authException);
    }
}
