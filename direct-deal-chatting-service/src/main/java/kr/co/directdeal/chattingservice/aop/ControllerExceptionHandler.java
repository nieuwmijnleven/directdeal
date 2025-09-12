package kr.co.directdeal.chattingservice.aop;

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

import kr.co.directdeal.chattingservice.exception.ChattingException;
import kr.co.directdeal.chattingservice.exception.ChattingRoomAlreadyCreatedException;
import lombok.RequiredArgsConstructor;

/**
 * Global Exception Handler (Controller Advice)
 *
 * Captures various exceptions thrown by Spring MVC controllers
 * and returns appropriate HTTP status codes and messages.
 *
 * Main responsibilities:
 * - Handle authentication and authorization failures (401, 403, 400)
 * - Handle custom exceptions related to the chatting service
 * - Handle validation errors
 * - Support internationalized messages using MessageSource
 *
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles authentication failures.
     * HTTP Status: 401 Unauthorized
     */
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleException(AuthenticationException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles access denied (authorization) failures.
     * HTTP Status: 403 Forbidden
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(AccessDeniedException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles bad credentials exceptions.
     * HTTP Status: 400 Bad Request
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles general chatting service exceptions.
     * HTTP Status: 400 Bad Request
     * The error message is retrieved from the internationalized message source.
     */
    @ExceptionHandler({ChattingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ChattingException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Chatting Service Error", message);
    }

    /**
     * Handles exceptions when a chatting room is already created.
     * HTTP Status: 409 Conflict
     * The error message is retrieved from the internationalized message source.
     */
    @ExceptionHandler({ChattingRoomAlreadyCreatedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleException(ChattingRoomAlreadyCreatedException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Chatting Service Error", message);
    }

    /**
     * Handles validation errors (e.g., when @Valid validation fails).
     * HTTP Status: 400 Bad Request
     * The error message is retrieved from the message source for the first failed field.
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(BindException ex) {
        String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Validation Error", message);
    }

    // Optionally, a catch-all handler for all exceptions can be added here
    // @ExceptionHandler({Exception.class})
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ErrorResponse handleException(Exception ex) {
    //     return new ErrorResponse("Server Error", ex.getMessage());
    // }
}
