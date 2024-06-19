package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record CreateMemberCouponResponse(String code) {
	public static CreateMemberCouponResponse fromEntity(MemberCoupon coupon) {
		return CreateMemberCouponResponse.builder().code(coupon.getCouponCode()).build();
	}
}
