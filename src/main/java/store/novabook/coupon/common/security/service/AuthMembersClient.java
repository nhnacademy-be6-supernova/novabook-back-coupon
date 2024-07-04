package store.novabook.coupon.common.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.coupon.common.security.dto.request.GetMembersUUIDRequest;
import store.novabook.coupon.common.security.dto.response.GetMembersUUIDResponse;

@FeignClient(name = "authMemberClient", url = "http://localhost:9777/auth/members/uuid")
public interface AuthMembersClient {
	@PostMapping
	GetMembersUUIDResponse getMembersId(@RequestBody GetMembersUUIDRequest getMembersUUIDRequest);

}