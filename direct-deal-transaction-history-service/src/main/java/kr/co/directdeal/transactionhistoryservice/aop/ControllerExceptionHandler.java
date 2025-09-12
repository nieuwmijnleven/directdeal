package kr.co.directdeal.transactionhistoryservice.aop;

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

import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
import lombok.RequiredArgsConstructor;

/**
 * Global exception handler for REST controllers.
 *
 * <p>This class intercepts and processes exceptions thrown by controllers,
 * mapping them to appropriate HTTP responses and localized error messages.
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles any uncaught general exceptions.
     *
     * @param ex the thrown exception
     * @return error response with HTTP 500 status
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Server Error", ex.getMessage());
    }

    /**
     * Handles authentication-related exceptions.
     *
     * @param ex the thrown AuthenticationException
     * @return error response with HTTP 401 status
     */
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleException(AuthenticationException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles access denied (authorization) exceptions.
     *
     * @param ex the thrown AccessDeniedException
     * @return error response with HTTP 403 status
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(AccessDeniedException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles bad credentials exceptions.
     *
     * @param ex the thrown BadCredentialsException
     * @return error response with HTTP 400 status
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles exceptions related to transaction history.
     *
     * @param ex the thrown TransactionHistoryException
     * @return localized error response with HTTP 400 status
     */
    @ExceptionHandler({TransactionHistoryException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(TransactionHistoryException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Transaction History Service Error", message);
    }

    /**
     * Handles exceptions related to buy history.
     *
     * @param ex the thrown BuyHistoryException
     * @return localized error response with HTTP 400 status
     */
    @ExceptionHandler({BuyHistoryException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BuyHistoryException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Buy History Service Error", message);
    }

    /**
     * Handles validation binding errors for request objects.
     *
     * @param ex the thrown BindException
     * @return localized error response with HTTP 400 status
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(BindException ex) {
        String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Validation Error", message);
    }
}
