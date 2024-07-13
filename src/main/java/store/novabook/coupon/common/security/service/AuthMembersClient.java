package store.novabook.coupon.common.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import store.novabook.coupon.common.dto.ApiResponse;
import store.novabook.coupon.common.security.dto.request.GetMembersUUIDRequest;
import store.novabook.coupon.common.security.dto.response.GetMembersUUIDResponse;

/**
 * 인증된 회원의 UUID를 가져오는 클라이언트 인터페이스.
 */
@FeignClient(name = "authMemberClient", url = "http://localhost:9777/auth/members/uuid")
public interface AuthMembersClient {

	/**
	 * 회원의 UUID를 가져옵니다.
	 *
	 * @param getMembersUUIDRequest 회원 UUID 요청 객체
	 * @return 회원 UUID 응답 객체
	 */
	@PostMapping
	ApiResponse<GetMembersUUIDResponse> getMembersId(@Valid @RequestBody GetMembersUUIDRequest getMembersUUIDRequest);
}
