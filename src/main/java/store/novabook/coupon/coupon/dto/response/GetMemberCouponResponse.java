package store.novabook.coupon.coupon.dto.response;

import java.util.List;

public record GetMemberCouponResponse(
	List<GetCouponResponse> couponList
) {
}
