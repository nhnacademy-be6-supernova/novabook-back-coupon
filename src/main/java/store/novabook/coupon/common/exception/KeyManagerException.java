package store.novabook.coupon.common.exception;

/**
 * 키 관리자(Key Manager) 관련 오류를 나타내는 예외 클래스입니다.
 * <p>
 * 주어진 에러 코드와 함께 예외를 생성합니다.
 * </p>
 */
public class KeyManagerException extends NovaException {

	/**
	 * 주어진 에러 코드로 KeyManagerException을 생성합니다.
	 *
	 * @param errorCode 발생한 에러 코드
	 */
	public KeyManagerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
