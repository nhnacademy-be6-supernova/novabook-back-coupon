package store.novabook.coupon.common.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.coupon.common.security.dto.request.GetNewTokenRequest;
import store.novabook.coupon.common.security.dto.response.GetNewTokenResponse;

@FeignClient(name = "newTokenClient", url = "http://localhost:9777/auth/refresh")
public interface NewTokenClient {
	@PostMapping
	GetNewTokenResponse getNewToken(@RequestBody GetNewTokenRequest getNewTokenRequest);
}