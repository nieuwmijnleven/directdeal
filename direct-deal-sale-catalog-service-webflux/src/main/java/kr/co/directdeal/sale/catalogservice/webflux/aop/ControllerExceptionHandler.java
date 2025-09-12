package kr.co.directdeal.sale.catalogservice.webflux.aop;

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

import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.webflux.exception.SaleListException;
import lombok.RequiredArgsConstructor;

/**
 * Global exception handler for REST controllers.
 * <p>
 * Handles exceptions thrown from controller layers and returns
 * appropriate HTTP status codes along with standardized error responses.
 * </p>
 *
 * Handles custom business exceptions, authentication failures,
 * access denials, and validation errors.
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles unexpected server-side exceptions.
     *
     * @param ex the unhandled exception
     * @return an error response with internal server error status
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Server Error", ex.getMessage());
    }

    /**
     * Handles Spring Security authentication failures.
     *
     * @param ex the authentication exception
     * @return an error response with 401 status
     */
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleException(AuthenticationException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles access denied exceptions, usually due to insufficient permissions.
     *
     * @param ex the access denied exception
     * @return an error response with 403 status
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(AccessDeniedException ex) {
        return new ErrorResponse("Access Denied", ex.getMessage());
    }

    /**
     * Handles bad credentials (e.g., wrong username/password).
     *
     * @param ex the bad credentials exception
     * @return an error response with 400 status
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("Invalid Credentials", ex.getMessage());
    }

    /**
     * Handles custom exceptions thrown from sale item operations.
     *
     * @param ex the SaleItemException
     * @return an error response with localized message
     */
    @ExceptionHandler({SaleItemException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(SaleItemException ex) {
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getMessageArgs(),
                LocaleContextHolder.getLocale()
        );
        return new ErrorResponse("Sale Item Service Error", message);
    }

    /**
     * Handles custom exceptions thrown from sale list operations.
     *
     * @param ex the SaleListException
     * @return an error response with localized message
     */
    @ExceptionHandler({SaleListException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(SaleListException ex) {
        String message = messageSource.getMessage(
                ex.getMessageKey(),
                ex.getMessageArgs(),
                LocaleContextHolder.getLocale()
        );
        return new ErrorResponse("Sale List Service Error", message);
    }

    /**
     * Handles validation errors thrown by Spring's data binding.
     *
     * @param ex the BindException
     * @return an error response with the first field error's message
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
