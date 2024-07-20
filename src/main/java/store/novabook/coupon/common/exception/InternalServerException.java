package store.novabook.coupon.common.exception;

/**
 * 서버 내부 오류를 나타내는 예외 클래스입니다.
 * <p>
 * 주어진 에러 코드와 함께 예외를 생성합니다.
 * </p>
 */
public class InternalServerException extends NovaException {

	/**
	 * 주어진 에러 코드로 InternalServerException을 생성합니다.
	 *
	 * @param errorCode 발생한 에러 코드
	 */
	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
