package store.novabook.coupon.common.hanlder;

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
import store.novabook.coupon.common.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		log.info("{}", body);

		if (body instanceof ErrorResponse errorResponse) {
			return ApiResponse.error(errorResponse.errorCode(), errorResponse);
		}
		if (body instanceof ValidErrorResponse validErrorResponse) {
			return ApiResponse.error(ErrorCode.INVALID_REQUEST_ARGUMENT, validErrorResponse);
		}

		if (body instanceof Page) {
			Page<?> page = (Page<?>)body;
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