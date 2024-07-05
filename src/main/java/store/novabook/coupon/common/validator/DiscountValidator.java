package store.novabook.coupon.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.Discountable;

/**
 * {@code DiscountValidator} 클래스는 할인 금액의 유효성을 검증하는 유효성 검사기입니다.
 * 할인 유형에 따라 적절한 범위 내의 할인 금액을 검증합니다.
 */
public class DiscountValidator implements ConstraintValidator<ValidDiscount, Discountable> {

	public static final int DISCOUNT_MIN_PERCENT = 0;
	public static final int DISCOUNT_MIN_AMOUNT = 0;
	public static final int DISCOUNT_MAX_PERCENT = 100;
	public static final int DISCOUNT_MAX_AMOUNT = 100000;

	/**
	 * 할인 금액의 유효성을 검증합니다.
	 *
	 * @param request {@code Discountable} 객체
	 * @param context 유효성 검사 컨텍스트
	 * @return 유효한 경우 {@code true}, 그렇지 않은 경우 {@code false}
	 */
	@Override
	public boolean isValid(Discountable request, ConstraintValidatorContext context) {
		boolean isValid = true;

		if (request.getDiscountType() == DiscountType.PERCENT) {
			if (request.getDiscountAmount() < DISCOUNT_MIN_PERCENT
				|| request.getDiscountAmount() > DISCOUNT_MAX_PERCENT) {
				context.buildConstraintViolationWithTemplate("할인 금액은 0에서 100 사이여야 합니다.")
					.addPropertyNode("discountAmount")
					.addConstraintViolation();
				isValid = false;
			}
		} else if (request.getDiscountType() == DiscountType.AMOUNT && (
			request.getDiscountAmount() < DISCOUNT_MIN_AMOUNT || request.getDiscountAmount() > DISCOUNT_MAX_AMOUNT)) {
			context.buildConstraintViolationWithTemplate("할인 금액은 0에서 100,000 사이여야 합니다.")
				.addPropertyNode("discountAmount")
				.addConstraintViolation();
			isValid = false;
		}

		context.disableDefaultConstraintViolation();
		return isValid;
	}
}
