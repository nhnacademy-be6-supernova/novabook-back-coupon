package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.DiscountType;

@Builder
public record GetCouponCategoryResponse(Long categoryId, String code, String name, long discountAmount,
										DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
										LocalDateTime startedAt, LocalDateTime expirationAt, int usePeriod) {
	public static GetCouponCategoryResponse fromEntity(CategoryCoupon categoryCoupon) {
		return GetCouponCategoryResponse.builder()
			.categoryId(categoryCoupon.getCategoryId())
			.code(categoryCoupon.getCouponCode())
			.name(categoryCoupon.getCoupon().getName())
			.discountAmount(categoryCoupon.getCoupon().getDiscountAmount())
			.discountType(categoryCoupon.getCoupon().getDiscountType())
			.maxDiscountAmount(categoryCoupon.getCoupon().getMaxDiscountAmount())
			.minPurchaseAmount(categoryCoupon.getCoupon().getMinPurchaseAmount())
			.startedAt(categoryCoupon.getCoupon().getStartedAt())
			.expirationAt(categoryCoupon.getCoupon().getExpirationAt())
			.usePeriod(categoryCoupon.getCoupon().getUsePeriod())
			.build();
	}
}
