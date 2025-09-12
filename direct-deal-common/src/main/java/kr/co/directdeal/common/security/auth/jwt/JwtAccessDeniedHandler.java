package kr.co.directdeal.common.security.auth.jwt;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;

/**
 * Handles AccessDeniedException by delegating to a Spring MVC HandlerExceptionResolver.
 * This allows consistent exception handling for access denied errors in JWT authentication.
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}
