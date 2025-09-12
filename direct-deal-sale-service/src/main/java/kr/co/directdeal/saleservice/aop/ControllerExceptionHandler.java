package kr.co.directdeal.saleservice.aop;

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
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import kr.co.directdeal.saleservice.exception.FavoriteItemException;
import kr.co.directdeal.saleservice.exception.ItemCategoryException;
import kr.co.directdeal.saleservice.exception.ItemImageException;
import kr.co.directdeal.saleservice.exception.SaleItemException;
import lombok.RequiredArgsConstructor;

/**
 * Global exception handler for REST controllers.
 * Handles various exceptions thrown within the application and returns appropriate HTTP status codes and error messages.
 * Localization of error messages is supported via {@link MessageSource}.
 *
 * <p>This class uses {@code @RestControllerAdvice} to intercept exceptions globally.
 * Typically, Swagger annotations are not applied to exception handlers,
 * as they are not direct API endpoints.</p>
 *
 * @author Cheol Jeon
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles generic exceptions.
     *
     * @param ex Exception instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse("Server Error", ex.getMessage());
    }

    /**
     * Handles authentication-related exceptions.
     *
     * @param ex AuthenticationException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleException(AuthenticationException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles access denied exceptions.
     *
     * @param ex AccessDeniedException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleException(AccessDeniedException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles bad credentials exceptions.
     *
     * @param ex BadCredentialsException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(BadCredentialsException ex) {
        return new ErrorResponse("Authentication Failed", ex.getMessage());
    }

    /**
     * Handles file upload missing part exceptions.
     *
     * @param ex MissingServletRequestPartException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(MissingServletRequestPartException ex) {
        return new ErrorResponse("File Upload Error", ex.getMessage());
    }

    /**
     * Handles SaleItem service related exceptions.
     * Localizes message using message source.
     *
     * @param ex SaleItemException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({SaleItemException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(SaleItemException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Sale Item Service Error", message);
    }

    /**
     * Handles ItemImage service related exceptions.
     * Localizes message using message source.
     *
     * @param ex ItemImageException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({ItemImageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ItemImageException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Item Image Service Error", message);
    }

    /**
     * Handles FavoriteItem service related exceptions.
     * Localizes message using message source.
     *
     * @param ex FavoriteItemException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({FavoriteItemException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(FavoriteItemException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Favorite Item Service Error", message);
    }

    /**
     * Handles ItemCategory service related exceptions.
     * Localizes message using message source.
     *
     * @param ex ItemCategoryException instance
     * @return ErrorResponse containing error details
     */
    @ExceptionHandler({ItemCategoryException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ItemCategoryException ex) {
        String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Item Category Service Error", message);
    }

    /**
     * Handles validation errors from data binding.
     * Localizes message using message source.
     *
     * @param ex BindException instance
     * @return ErrorResponse containing validation error details
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(BindException ex) {
        String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
        return new ErrorResponse("Validation Error", message);
    }
}
