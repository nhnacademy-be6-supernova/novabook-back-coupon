package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;

@Builder
public record CreateCouponResponse(String code) {
	public static CreateCouponResponse fromEntity(Coupon coupon) {
		return CreateCouponResponse.builder()
			.code(coupon.getCode())
			.build();
	}

	public static CreateCouponResponse fromEntity(BookCoupon coupon) {
		return CreateCouponResponse.builder()
			.code(coupon.getCouponCode())
			.build();
	}

	public static CreateCouponResponse fromEntity(CategoryCoupon coupon) {
		return CreateCouponResponse.builder()
			.code(coupon.getCouponCode())
			.build();
	}
}
