package kr.co.directdeal.sale.catalogservice.aop;

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

import kr.co.directdeal.sale.catalogservice.exception.SaleItemException;
import kr.co.directdeal.sale.catalogservice.exception.SaleListException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice
//@RestControllerAdvice(basePackages = {"kr.co.directdeal.accountservice.adapter.inbound"})
public class ControllerExceptionHandler {
	
	private final MessageSource messageSource;

	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(Exception ex) {
		return new ErrorResponse("Server Error", ex.getMessage());
	}

	@ExceptionHandler({AuthenticationException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleException(AuthenticationException ex) {
		return new ErrorResponse("Authentication Failed", ex.getMessage());
	}

	@ExceptionHandler({AccessDeniedException.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleException(AccessDeniedException ex) {
		return new ErrorResponse("Authentication Failed", ex.getMessage());
	}

	@ExceptionHandler({BadCredentialsException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(BadCredentialsException ex) {
		return new ErrorResponse("Authentication Failed", ex.getMessage());
	}

	@ExceptionHandler({SaleItemException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(SaleItemException ex) {
		String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Sale Item Service Error", message);
	}

	@ExceptionHandler({SaleListException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(SaleListException ex) {
		String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Sale List Service Error", message);
	}
	
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidationError(BindException ex) {
		String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Validation Error", message);
	}
}
