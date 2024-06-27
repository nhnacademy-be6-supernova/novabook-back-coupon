package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;

@Builder
public record GetCouponAllResponse(List<GetCouponResponse> couponResponseList) {
	public static GetCouponAllResponse fromEntity(List<Coupon> couponList) {
		return GetCouponAllResponse.builder()
			.couponResponseList(couponList.stream().map(GetCouponResponse::fromEntity).toList())
			.build();
	}
}
