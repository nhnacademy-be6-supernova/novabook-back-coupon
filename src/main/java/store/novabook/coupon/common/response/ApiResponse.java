package store.novabook.coupon.common.response;

import java.util.HashMap;
import java.util.Map;

import store.novabook.coupon.common.exception.ErrorCode;

public class ApiResponse<T> {

	private Map<String, Object> header = new HashMap<>();
	private T body;

	public ApiResponse(int resultCode, String resultMessage, boolean isSuccessful, T body) {
		this.header.put("resultCode", resultCode);
		this.header.put("resultMessage", resultMessage);
		this.header.put("isSuccessful", isSuccessful);
		this.body = body;
	}

	public static <T> ApiResponse<T> success(T body) {
		return new ApiResponse<>(0, "SUCCESS", true, body);
	}

	public static <T> ApiResponse<T> error(ErrorCode errorCode, T body) {
		return new ApiResponse<>(errorCode.ordinal(), "FAILED", false, body);
	}

	public Map<String, Object> getHeader() {
		return header;
	}

	public T getBody() {
		return body;
	}
}