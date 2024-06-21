package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.BookCoupon;

@Builder
public record GetCouponBookAllResponse(List<GetCouponResponse> couponResponseList) {
	public static GetCouponBookAllResponse fromEntity(List<BookCoupon> bookCouponList) {
		List<GetCouponResponse> couponResponseList = bookCouponList.stream()
			.map(bookCoupon -> GetCouponResponse.fromEntity(bookCoupon.getCoupon()))
			.toList();

		return GetCouponBookAllResponse.builder().couponResponseList(couponResponseList).build();
	}
}
