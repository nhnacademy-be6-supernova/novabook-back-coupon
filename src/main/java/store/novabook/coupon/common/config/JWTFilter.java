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
import store.novabook.coupon.common.security.entity.AuthMembers;
import store.novabook.coupon.common.security.service.AuthMembersClient;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final AuthMembersClient authMembersClient;

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String username = request.getHeader("X-USER-ID");
		String role = request.getHeader("X-USER-ROLE");

		if (username == null || role == null) {
			log.error("username or role is null");
			filterChain.doFilter(request, response);
			return;
		}

		GetMembersUUIDRequest getMembersUUIDRequest = new GetMembersUUIDRequest(username);

		GetMembersUUIDResponse getMembersUUIDResponse = authMembersClient.getMembersId(getMembersUUIDRequest);

		//userEntity를 생성하여 값 set
		AuthMembers membersEntity = new AuthMembers();
		membersEntity.setId(Long.parseLong(getMembersUUIDResponse.membersId()));
		membersEntity.setUsername(getMembersUUIDResponse.membersId());
		membersEntity.setPassword("temppassword");
		membersEntity.setRole(role);

		//UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(membersEntity);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		//세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}