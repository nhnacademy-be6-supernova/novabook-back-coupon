package store.novabook.coupon.common.hanlder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import store.novabook.coupon.common.exception.dto.ValidErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// hibernate validation 실행 시 작동
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
		ValidErrorResponse response = ValidErrorResponse.from(e);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
