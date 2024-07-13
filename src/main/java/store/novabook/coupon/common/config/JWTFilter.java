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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.NonNull;
import store.novabook.coupon.common.security.dto.CustomUserDetails;
import store.novabook.coupon.common.security.dto.request.GetMembersUUIDRequest;
import store.novabook.coupon.common.security.dto.response.GetMembersUUIDResponse;
import store.novabook.coupon.common.security.entity.AuthenticationMembers;
import store.novabook.coupon.common.security.service.AuthMembersClient;

/**
 * JWTFilter 클래스는 요청 헤더에서 JWT 토큰을 처리하고 현재 세션에 대한 인증을 설정합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final AuthMembersClient authMembersClient;

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

		String username = request.getHeader("X-USER-ID");
		String role = request.getHeader("X-USER-ROLE");

		if (username == null || role == null) {
			log.error("username 또는 role이 null입니다");
			filterChain.doFilter(request, response);
			return;
		}

		GetMembersUUIDRequest getMembersUUIDRequest = new GetMembersUUIDRequest(username);
		GetMembersUUIDResponse getMembersUUIDResponse = authMembersClient.getMembersId(getMembersUUIDRequest).getBody();
		AuthenticationMembers authenticationMembers = AuthenticationMembers.of(
			getMembersUUIDResponse.membersId(),
			null,
			null,
			getMembersUUIDResponse.role()
		);

		CustomUserDetails customUserDetails = new CustomUserDetails(authenticationMembers);
		Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
