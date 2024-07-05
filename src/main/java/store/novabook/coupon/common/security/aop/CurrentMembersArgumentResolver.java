package store.novabook.coupon.common.security.aop;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link CurrentMembers} 어노테이션이 있는 파라미터를 처리하는 Argument Resolver.
 */
@Component
public class CurrentMembersArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 파라미터가 {@link CurrentMembers} 어노테이션을 가지고 있는지 확인합니다.
	 *
	 * @param parameter 메서드 파라미터
	 * @return {@link CurrentMembers} 어노테이션이 있으면 true, 그렇지 않으면 false
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(CurrentMembers.class) != null;
	}

	/**
	 * {@link CurrentMembers} 어노테이션이 있는 파라미터의 값을 해석하여 반환합니다.
	 *
	 * @param parameter     메서드 파라미터
	 * @param mavContainer  ModelAndViewContainer 객체
	 * @param webRequest    NativeWebRequest 객체
	 * @param binderFactory WebDataBinderFactory 객체
	 * @return 현재 인증된 사용자의 ID
	 * @throws Exception 해석 중 예외가 발생한 경우
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return Long.parseLong(authentication.getName());
		}
		return null;
	}
}
