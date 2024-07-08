package store.novabook.coupon.common.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import store.novabook.coupon.common.adapter.dto.CreateMemberCouponResponse;
import store.novabook.coupon.common.adapter.dto.RegisterCouponRequest;
import store.novabook.coupon.common.dto.ApiResponse;

/**
 * {@code StoreAdapter} 인터페이스는 외부의 스토어 서비스와의 통신을 위한 Feign 클라이언트를 정의합니다.
 */
@FeignClient(name = "store-service")
public interface StoreAdapter {

	@PostMapping("/members/coupons/register")
	ApiResponse<CreateMemberCouponResponse> registerCoupon(@RequestHeader("Authorization") String token,
		@RequestHeader("Refresh") String refresh,
		@RequestBody RegisterCouponRequest request);
}
