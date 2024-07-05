package store.novabook.coupon.common.security.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드에 적용할 수 있는 역할 체크 어노테이션.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
	/**
	 * 필요한 역할을 지정합니다.
	 *
	 * @return 역할 값
	 */
	String value();
}
