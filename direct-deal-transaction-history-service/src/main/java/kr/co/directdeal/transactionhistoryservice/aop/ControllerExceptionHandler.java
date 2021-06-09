package kr.co.directdeal.transactionhistoryservice.aop;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.directdeal.transactionhistoryservice.exception.BuyHistoryException;
import kr.co.directdeal.transactionhistoryservice.exception.TransactionHistoryException;
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

	// @ExceptionHandler({AuthenticationException.class})
	// @ResponseStatus(HttpStatus.UNAUTHORIZED)
	// public ErrorResponse handleException(AuthenticationException ex) {
	// 	return new ErrorResponse("Authentication Failed", ex.getMessage());
	// }

	// @ExceptionHandler({AccessDeniedException.class})
	// @ResponseStatus(HttpStatus.FORBIDDEN)
	// public ErrorResponse handleException(AccessDeniedException ex) {
	// 	return new ErrorResponse("Authentication Failed", ex.getMessage());
	// }

	// @ExceptionHandler({BadCredentialsException.class})
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// public ErrorResponse handleException(BadCredentialsException ex) {
	// 	return new ErrorResponse("Authentication Failed", ex.getMessage());
	// }

	@ExceptionHandler({TransactionHistoryException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(TransactionHistoryException ex) {
		String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Transaction History Service Error", message);
	}

	@ExceptionHandler({BuyHistoryException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleException(BuyHistoryException ex) {
		String message = messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Buy History Service Error", message);
	}
	
	@ExceptionHandler({BindException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidationError(BindException ex) {
		String message = messageSource.getMessage(ex.getFieldError(), LocaleContextHolder.getLocale());
		return new ErrorResponse("Validation Error", message);
	}
}
