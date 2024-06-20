package store.novabook.coupon.common.exception;

public class ForbiddenException extends NovaException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}
}
