package store.novabook.coupon.common.exception;

public enum ErrorCode {

	// 400
	INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),
	EXPIRED_COUPON_CODE("만료된 쿠폰 코드입니다."),

	// 404
	COUPON_NOT_FOUND("존재하지 않는 쿠폰입니다."),
	BOOK_COUPON_NOT_FOUND("해당 책에 대한 쿠폰이 존재하지 않습니다."),
	CATEGORY_COUPON_NOT_FOUND("해당 카테고리에 대한 쿠폰이 존재하지 않습니다."),

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
