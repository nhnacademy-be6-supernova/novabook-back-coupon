package store.novabook.coupon.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * {@code ValidDiscount} 애노테이션은 할인 정보의 유효성을 검증하는 유효성 검사 애노테이션입니다.
 * 클래스 레벨에서 적용되며, {@link DiscountValidator}를 통해 유효성을 검사합니다.
 */
@Constraint(validatedBy = DiscountValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDiscount {

	/**
	 * 유효성 검사 실패 시 반환할 기본 메시지입니다.
	 *
	 * @return 기본 메시지
	 */
	String message() default "유효하지 않은 할인 정보입니다.";

	/**
	 * 유효성 검사 그룹을 정의합니다.
	 *
	 * @return 유효성 검사 그룹
	 */
	Class<?>[] groups() default {};

	/**
	 * 유효성 검사 시 사용할 페이로드를 정의합니다.
	 *
	 * @return 페이로드 클래스 배열
	 */
	Class<? extends Payload>[] payload() default {};
}
