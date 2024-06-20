package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record CreateMemberCouponResponse(Long id) {
	public static CreateMemberCouponResponse fromEntity(MemberCoupon coupon) {
		return CreateMemberCouponResponse.builder().id(coupon.getId()).build();
	}
}
