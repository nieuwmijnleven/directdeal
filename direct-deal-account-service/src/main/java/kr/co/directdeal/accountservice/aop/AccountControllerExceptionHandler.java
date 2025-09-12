package kr.co.directdeal.accountservice.aop;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.directdeal.accountservice.exception.AccountException;
import lombok.RequiredArgsConstructor;

/**
 * Global exception handler for account-related controllers.
 *
 * <p>This class intercepts exceptions thrown from the controller layer and
 * provides unified error responses to the client.</p>
 *
 * <p>Supports internationalized error messages via {@link MessageSource}.</p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestControllerAdvice
//@RestControllerAdvice(basePackages = {"kr.co.directdeal.accountservice.adapter.inbound"})
public class AccountControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles all uncaught exceptions (fallback).
     *
     * @param ex the exception thrown
     * @return generic server error response
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Server Error", ex.getMessage());
    }

    /**
     * Handles Spring Security authentication exceptions (e.g., invalid JWT).
     *
     * @param ex the authentication exception
     * @return unauthorized error response
     */
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleException(AuthenticationException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles authorization exceptions (access denied).
     *
     * @param ex the access denied exception
     * @return forbidden error response
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(AccessDeniedException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles bad credentials (e.g., wrong email/password).
     *
     * @param ex the bad credentials exception
     * @return bad request error response
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles application-specific account exceptions with internationalized messages.
     *
     * @param ex the custom account exception
     * @return account error response
     */
    @ExceptionHandler({AccountException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccountException(AccountException ex) {
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getMessageArgs(),
                LocaleContextHolder.getLocale()
        );
        return new ErrorResponse("Account Error", message);
    }

    /**
     * Handles validation errors on request binding (e.g., @Valid failures).
     *
     * @param ex the binding exception
     * @return validation error response with localized field error message
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(BindException ex) {
        String message = messageSource.getMessage(
                ex.getFieldError(),
                LocaleContextHolder.getLocale()
        );
        return new ErrorResponse("Validation Error", message);
    }
}
