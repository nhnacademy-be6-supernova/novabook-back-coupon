package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;

@Builder
public record CreateMemberCouponAllResponse(List<CreateCouponResponse> memberCouponResponseList) {
	public static CreateMemberCouponAllResponse fromEntity(List<Coupon> couponList) {
		return CreateMemberCouponAllResponse.builder()
			.memberCouponResponseList(couponList.stream().map(CreateCouponResponse::fromEntity).toList())
			.build();
	}
}
