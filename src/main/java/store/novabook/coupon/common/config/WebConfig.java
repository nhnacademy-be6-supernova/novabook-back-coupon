package store.novabook.coupon.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.security.aop.CurrentMembersArgumentResolver;

/**
 * Spring Web MVC 설정 클래스.
 * 커스텀 Argument Resolver를 추가합니다.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final CurrentMembersArgumentResolver currentMembersArgumentResolver;

	/**
	 * Argument Resolver를 추가합니다.
	 *
	 * @param resolvers HandlerMethodArgumentResolver 목록
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentMembersArgumentResolver);
	}
}
