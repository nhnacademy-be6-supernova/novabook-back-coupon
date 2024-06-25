package store.novabook.coupon.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.Discountable;

public class DiscountValidator implements ConstraintValidator<ValidDiscount, Discountable> {

	public static final int DISCOUNT_MIN_PERCENT = 0;
	public static final int DISCOUNT_MIN_AMOUNT = 0;
	public static final int DISCOUNT_MAX_PERCENT = 100;
	public static final int DISCOUNT_MAX_AMOUNT = 100000;

	@Override
	public boolean isValid(Discountable request, ConstraintValidatorContext context) {
		boolean isValid = true;

		// 할인 유형에 따른 할인 금액 검증
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
