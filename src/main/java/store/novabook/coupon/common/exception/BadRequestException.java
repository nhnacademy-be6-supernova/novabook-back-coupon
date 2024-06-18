package store.novabook.coupon.common.exception;

public class BadRequestException extends NovaException {
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
