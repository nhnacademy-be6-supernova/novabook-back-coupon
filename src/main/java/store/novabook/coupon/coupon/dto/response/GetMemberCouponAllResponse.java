package store.novabook.coupon.coupon.dto.response;

import org.springframework.data.domain.Page;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record GetMemberCouponAllResponse(Long memberId, Page<GetMemberCouponResponse> memberCouponPage) {
	public static GetMemberCouponAllResponse fromEntity(Long memberId, Page<MemberCoupon> memberCouponPage) {
		Page<GetMemberCouponResponse> couponResponsePage = memberCouponPage.map(GetMemberCouponResponse::fromEntity);
		return GetMemberCouponAllResponse.builder().memberId(memberId).memberCouponPage(couponResponsePage).build();
	}
}
