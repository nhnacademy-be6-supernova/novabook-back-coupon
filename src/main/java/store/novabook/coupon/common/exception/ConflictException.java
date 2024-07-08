package store.novabook.coupon.common.exception;

public class ConflictException extends NovaException {
	/**
	 * 지정된 오류 코드를 사용하여 새로운 {@code ConflictException}을 생성합니다.
	 *
	 * @param errorCode 발생한 오류에 대한 코드
	 */
	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}
}
