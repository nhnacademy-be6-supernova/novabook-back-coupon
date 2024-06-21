package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;

@Builder
public record GetMemberCouponByTypeResponse(List<GetMemberCouponResponse> generalCouponList,
											List<MemberBookCouponDto> bookCouponList,
											List<MemberCategoryCouponDto> categoryCouponList) {
}
