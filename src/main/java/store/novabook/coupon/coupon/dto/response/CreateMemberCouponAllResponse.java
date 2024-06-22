package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record CreateMemberCouponAllResponse(List<CreateMemberCouponResponse> memberCouponResponseList) {
	public static CreateMemberCouponAllResponse fromEntity(List<MemberCoupon> memberCouponList) {
		return CreateMemberCouponAllResponse.builder()
			.memberCouponResponseList(memberCouponList.stream().map(CreateMemberCouponResponse::fromEntity).toList())
			.build();
	}
}
