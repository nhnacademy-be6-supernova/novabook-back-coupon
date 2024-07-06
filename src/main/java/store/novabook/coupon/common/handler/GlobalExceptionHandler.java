package store.novabook.coupon.common.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.dto.ErrorResponse;
import store.novabook.coupon.common.dto.ValidErrorResponse;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.exception.NovaException;

/**
 * {@code GlobalExceptionHandler} 클래스는 애플리케이션 전역에서 발생하는 예외를 처리하는 핸들러입니다.
 * 다양한 예외 유형에 대해 적절한 HTTP 상태 코드와 함께 응답을 반환합니다.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * {@inheritDoc}
	 * <p>
	 * 유효하지 않은 메서드 인자 예외를 처리하여 {@link ValidErrorResponse}를 반환합니다.
	 * </p>
	 *
	 * @param exception 유효하지 않은 메서드 인자 예외
	 * @param headers   응답 헤더
	 * @param status    HTTP 상태 코드
	 * @param request   웹 요청
	 * @return {@link ValidErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("handleMethodArgumentNotValid - Exception: {} | Request: {}", exception.getMessage(),
			request.getDescription(false));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	/**
	 * {@code NotFoundException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception {@code NotFoundException}
	 * @param request   HTTP 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception,
		HttpServletRequest request) {
		log.error("handleNotFoundException - Exception: {} | URI: {}", exception.getMessage(), request.getRequestURI(),
			exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
	}

	/**
	 * {@code ForbiddenException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception {@code ForbiddenException}
	 * @param request   HTTP 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception,
		HttpServletRequest request) {
		log.error("handleForbiddenException - Exception: {} | URI: {}", exception.getMessage(), request.getRequestURI(),
			exception);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.from(exception));
	}

	/**
	 * {@code NovaException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception {@code NovaException}
	 * @param request   웹 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(NovaException.class)
	protected ResponseEntity<Object> handleNovaException(NovaException exception, WebRequest request) {
		log.error("handleNovaException - Exception: {} | Request: {}", exception.getMessage(),
			request.getDescription(false), exception);
		ErrorResponse errorResponse = ErrorResponse.from(exception);
		return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * 일반 {@code Exception}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception 일반 {@code Exception}
	 * @param request   HTTP 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
		log.error("handleException - Exception: {} | URI: {}", exception.getMessage(), request.getRequestURI(),
			exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	/**
	 * {@code MethodArgumentTypeMismatchException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param ex {@code MethodArgumentTypeMismatchException}
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		log.error("handleTypeMismatch - Exception: {}", ex.getMessage(), ex);
		return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_ARGUMENT_TYPE));
	}
}
