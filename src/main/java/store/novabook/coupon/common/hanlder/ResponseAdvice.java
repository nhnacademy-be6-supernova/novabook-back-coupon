package store.novabook.coupon.common.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.dto.ErrorResponse;
import store.novabook.coupon.common.exception.dto.ValidErrorResponse;
import store.novabook.coupon.common.dto.ApiResponse;

/**
 * {@code ResponseAdvice} 클래스는 클라이언트에게 응답을 작성하기 전에 응답 본문을 처리하는
 * 컨트롤러 어드바이스입니다. 다양한 유형의 응답 본문을 표준 {@link ApiResponse} 형식으로 변환하는 작업을 처리합니다.
 * <p>
 * 이 클래스는 응답 본문을 후처리하여 로깅하거나, 페이지 정보를 포함하거나, 오류 응답을 처리합니다.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * {@inheritDoc}
	 * <p>
	 * 모든 응답에 대해 이 어드바이스를 적용합니다.
	 * </p>
	 *
	 * @param returnType      응답 본문의 타입
	 * @param converterType   사용될 메시지 컨버터 타입
	 * @return 항상 {@code true}를 반환하여 모든 응답에 대해 적용합니다.
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 응답 본문을 작성하기 전에 호출되며, 응답 본문을 표준 {@link ApiResponse} 형식으로 변환합니다.
	 * </p>
	 *
	 * @param body                    응답 본문
	 * @param returnType              응답 본문의 타입
	 * @param selectedContentType     선택된 콘텐츠 타입
	 * @param selectedConverterType   선택된 메시지 컨버터 타입
	 * @param request                 서버 요청
	 * @param response                서버 응답
	 * @return 변환된 응답 본문을 반환합니다.
	 */
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
								  ServerHttpResponse response) {

		log.info("Response Body: {}", body);

		if (body instanceof ErrorResponse errorResponse) {
			return ApiResponse.error(errorResponse.errorCode(), errorResponse);
		}
		if (body instanceof ValidErrorResponse validErrorResponse) {
			return ApiResponse.error(ErrorCode.INVALID_REQUEST_ARGUMENT, validErrorResponse);
		}

		if (body instanceof Page<?> page) {
            Map<String, Object> pageBody = new HashMap<>();
			pageBody.put("pageNum", page.getNumber());
			pageBody.put("pageSize", page.getSize());
			pageBody.put("totalCount", page.getTotalElements());
			pageBody.put("data", page.getContent());
			return ApiResponse.success(pageBody);
		} else {
			return ApiResponse.success(body);
		}
	}
}
