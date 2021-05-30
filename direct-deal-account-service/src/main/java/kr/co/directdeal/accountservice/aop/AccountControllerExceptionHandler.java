package kr.co.directdeal.accountservice.aop;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.directdeal.accountservice.exception.AccountException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = {"kr.co.directdeal.accountservice.adapter.inbound"})
public class AccountControllerExceptionHandler {
	
	private final MessageSource messageSource;

	@ExceptionHandler({Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(Exception ex) {
		return new ErrorResponse("Server Error", ex.getMessage());
	}

	@ExceptionHandler({BadCredentialsException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(BadCredentialsException ex) {
		return new ErrorResponse("Authentication Error", ex.getMessage());
	}

	@ExceptionHandler({AccountException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleAccountException(AccountException ex) {
		String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Account Error", message);
	}
	
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidationError(BindException ex) {
		String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Validation Error", message);
	}
}
