package store.novabook.coupon.common.exception;

public class NotFoundException extends NovaException {
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
