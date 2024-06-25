package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;

@Builder
public record CreateCouponResponse(Long id) {
	public static CreateCouponResponse fromEntity(Coupon coupon) {
		return CreateCouponResponse.builder().id(coupon.getId()).build();
	}
}
