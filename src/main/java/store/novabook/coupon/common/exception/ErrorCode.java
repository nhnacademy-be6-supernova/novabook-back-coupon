package store.novabook.coupon.common.exception;

/**
 * 다양한 오류 상태를 나타내는 열거형 클래스입니다. 각 오류 상태는 HTTP 상태 코드와 관련된 메시지를 포함합니다.
 * 이 오류 코드는 Nova Book Store 애플리케이션 내에서 예외 처리에 사용됩니다.
 *
 * <p> 각 오류 코드는 다음과 같이 정의됩니다:
 * <ul>
 *     <li>400 - 잘못된 요청 오류</li>
 *     <li>404 - 리소스를 찾을 수 없음</li>
 *     <li>500 - 내부 서버 오류</li>
 * </ul>
 * </p>
 *
 * <p> 예제 사용법:
 * <pre>
 *     throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
 * </pre>
 * </p>
 */
public enum ErrorCode {

	// 400
	INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),
	EXPIRED_COUPON("만료된 쿠폰입니다."),
	INVALID_COUPON("유효하지 않은 쿠폰입니다."),
	INVALID_COUPON_TYPE("유효하지 않은 쿠폰 타입입니다."),
	WELCOME_COUPON_NOT_FOUND("웰컴 쿠폰이 존재하지 않습니다. "),
	BIRTHDAY_COUPON_NOT_FOUND("생일 쿠폰이 존재하지 않습니다. "),
	INVALID_ARGUMENT_TYPE("유효하지 않은 인자입니다."),
	ALREADY_USED_COUPON("이미 사용된 쿠폰입니다."),
	COUPON_ALREADY_EXIST("이미 다운받은 쿠폰입니다."),

	// 401 로그인 안됨
	UNAUTHORIZED("인증되지 않은 사용자입니다."),

	// 403
	NOT_ENOUGH_PERMISSION("해당 권한이 없습니다."),
	FORBIDDEN("접근 권한이 없습니다."),
	FORBIDDEN_COUPON_TYPE("해당 경로로는 선착순 쿠폰을 요청할 수 없습니다."),

	// 404
	COUPON_NOT_FOUND("해당 쿠폰이 존재하지 않습니다."),
	BOOK_COUPON_NOT_FOUND("해당 도서에 대한 쿠폰이 존재하지 않습니다."),
	CATEGORY_COUPON_NOT_FOUND("해당 카테고리에 대한 쿠폰이 존재하지 않습니다."),
	COUPON_TEMPLATE_NOT_FOUND("해당 쿠폰 템플릿이 존재하지 않습니다."),

	// 409
	CONFLICT_COUPON_TYPE("쿠폰 타입이 올바르지 않습니다. "),
	INSUFFICIENT_COUPON_QUANTITY("쿠폰이 모두 소진되었습니다."),

	// 500
	INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),

	FAILED_CONVERSION("Dto로 변환 하는데 실패했습니다."),
	RESPONSE_BODY_IS_NULL("키매니저의 response body가 null입니다."),
	MISSING_BODY_KEY("응답 본문에 \"body\" 키가 누락되었습니다."),
	MISSING_SECRET_KEY("응답 본문에 \"secret\" 키가 누락되었습니다.");

	private final String message;

	/**
	 * 주어진 메시지를 사용하여 새로운 {@code ErrorCode}를 생성합니다.
	 *
	 * @param message 오류 메시지
	 */
	ErrorCode(String message) {
		this.message = message;
	}

	/**
	 * 오류 메시지를 반환합니다.
	 *
	 * @return 오류 메시지
	 */
	public String getMessage() {
		return message;
	}
}
