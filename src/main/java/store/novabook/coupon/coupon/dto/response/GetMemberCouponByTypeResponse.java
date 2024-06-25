package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetMemberCouponByTypeResponse(List<GetCouponResponse> generalCouponList,
											List<GetMemberCouponBookResponse> bookCouponList,
											List<GetMemberCouponCategoryResponse> categoryCouponList) {
}
