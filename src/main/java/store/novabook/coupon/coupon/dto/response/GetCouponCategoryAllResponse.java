package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.CategoryCoupon;

@Builder
public record GetCouponCategoryAllResponse(List<GetCouponResponse> couponResponseList) {
	public static GetCouponCategoryAllResponse fromEntity(List<CategoryCoupon> bookCouponList) {
		List<GetCouponResponse> couponResponseList = bookCouponList.stream()
			.map(bookCoupon -> GetCouponResponse.fromEntity(bookCoupon.getCoupon()))
			.toList();

		return GetCouponCategoryAllResponse.builder().couponResponseList(couponResponseList).build();
	}
}
