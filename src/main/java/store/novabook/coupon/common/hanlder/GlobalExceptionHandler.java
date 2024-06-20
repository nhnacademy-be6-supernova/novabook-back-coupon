package store.novabook.coupon.common.hanlder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.exception.dto.ErrorResponse;
import store.novabook.coupon.common.exception.dto.ValidErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handle(NotFoundException exception, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handle(ForbiddenException exception, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.from(exception));
	}

	// @ExceptionHandler(Exception.class)
	// public ResponseEntity<ErrorResponse> handle(Exception exception, HttpServletRequest request) {
	// 	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 		.body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	// }

}
