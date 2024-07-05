package store.novabook.coupon.common.security.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.UnauthorizedException;

/**
 * 역할 검사를 위한 AOP 클래스.
 * 메서드 호출 전에 역할을 확인합니다.
 */
@Aspect
@Component
public class RoleCheckAspect {

	/**
	 * {@link CheckRole} 어노테이션이 있는 메서드가 호출되기 전에 역할을 검사합니다.
	 *
	 * @param joinPoint 조인 포인트
	 * @param checkRole {@link CheckRole} 어노테이션
	 * @throws UnauthorizedException 인증되지 않은 경우 발생
	 * @throws ForbiddenException    권한이 없는 경우 발생
	 */
	@Before("@annotation(checkRole)")
	public void checkRole(JoinPoint joinPoint, CheckRole checkRole) throws UnauthorizedException, ForbiddenException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}

		String requiredRole = checkRole.value();
		boolean hasRole = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(role -> role.equals(requiredRole));

		if (!hasRole) {
			throw new ForbiddenException(ErrorCode.FORBIDDEN);
		}
	}
}
