package store.novabook.coupon.coupon.dto.response;

import org.springframework.data.domain.Page;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record GetMemberCouponResponse(Long memberId, Page<GetCouponResponse> couponList) {
	public static GetMemberCouponResponse fromEntity(Long memberId, Page<MemberCoupon> memberCouponPage) {
		Page<GetCouponResponse> couponResponsePage = memberCouponPage.map(GetCouponResponse::fromEntity);
		return GetMemberCouponResponse.builder().memberId(memberId).couponList(couponResponsePage).build();
	}
}
