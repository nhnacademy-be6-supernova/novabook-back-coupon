package store.novabook.coupon.coupon.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateCouponExpirationRequest(
	@NotNull(message = "쿠폰 코드가 필요합니다.") String code
) {
}