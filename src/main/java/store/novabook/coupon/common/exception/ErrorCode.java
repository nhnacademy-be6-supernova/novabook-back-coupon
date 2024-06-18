package store.novabook.coupon.common.exception;

public enum ErrorCode {

	// 400
	INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),

	// 404
	COUPON_NOT_FOUND("존재하지 않는 쿠폰입니다."),

	// 500
	INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다.");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
