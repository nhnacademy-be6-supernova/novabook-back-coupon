package store.novabook.coupon.common.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.NonNull;
import store.novabook.coupon.common.security.dto.CustomUserDetails;
import store.novabook.coupon.common.security.entity.AuthenticationMembers;

/**
 * JWTFilter 클래스는 요청 헤더에서 JWT 토큰을 처리하고 현재 세션에 대한 인증을 설정합니다.
 */
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	/**
	 * 각 요청을 필터링하고 헤더에 username과 role이 있는 경우 인증을 설정합니다.
	 *
	 * @param request     HTTP 요청 객체.
	 * @param response    HTTP 응답 객체.
	 * @param filterChain 필터 체인.
	 * @throws ServletException 서블릿이 요청을 처리하는 동안 입력 또는 출력 오류가 발생한 경우.
	 * @throws IOException      요청을 처리할 수 없는 경우.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String membersId = request.getHeader("X-USER-ID");
		String role = request.getHeader("X-USER-ROLE");

		if (membersId == null || role == null) {
			log.error("username or role is null");
			filterChain.doFilter(request, response);
			return;
		}

		AuthenticationMembers authenticationMembers = AuthenticationMembers.of(
			Long.parseLong(membersId),
			null,
			null,
			role
		);

		CustomUserDetails customUserDetails = new CustomUserDetails(authenticationMembers);
		Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
