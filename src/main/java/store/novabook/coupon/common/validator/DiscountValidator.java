package store.novabook.coupon.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.Discountable;

public class DiscountValidator implements ConstraintValidator<ValidDiscount, Discountable> {

	@Override
	public void initialize(ValidDiscount constraintAnnotation) {
	}

	@Override
	public boolean isValid(Discountable request, ConstraintValidatorContext context) {
		boolean isValid = true;

		// 할인 유형에 따른 할인 금액 검증
		if (request.getDiscountType() == DiscountType.PERCENT) {
			if (request.getDiscountAmount() < 0 || request.getDiscountAmount() > 100) {
				context.buildConstraintViolationWithTemplate("할인 금액은 0에서 100 사이여야 합니다.")
					.addPropertyNode("discountAmount")
					.addConstraintViolation();
				isValid = false;
			}
		} else if (request.getDiscountType() == DiscountType.AMOUNT) {
			if (request.getDiscountAmount() < 0 || request.getDiscountAmount() > 100000) {
				context.buildConstraintViolationWithTemplate("할인 금액은 0에서 100,000 사이여야 합니다.")
					.addPropertyNode("discountAmount")
					.addConstraintViolation();
				isValid = false;
			}
		}

		context.disableDefaultConstraintViolation();
		return isValid;
	}
}
