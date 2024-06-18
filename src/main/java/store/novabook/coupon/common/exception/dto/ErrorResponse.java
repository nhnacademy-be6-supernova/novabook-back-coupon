package store.novabook.coupon.common.exception.dto;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NovaException;

public record ErrorResponse(ErrorCode errorCode, String message) {

	public static ErrorResponse from(NovaException novaException) {
		return ErrorResponse.from(novaException.getErrorCode());
	}

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode, errorCode.getMessage());
	}
}
