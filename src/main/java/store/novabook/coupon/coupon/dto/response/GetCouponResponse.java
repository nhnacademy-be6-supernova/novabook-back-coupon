package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;

@Builder
public record GetCouponResponse(String code, String name, long discountAmount, DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime startedAt,
								LocalDateTime expirationAt) {
	public static GetCouponResponse fromEntity(Coupon coupon) {
		return GetCouponResponse.builder()
			.code(coupon.getCode())
			.name(coupon.getName())
			.discountAmount(coupon.getDiscountAmount())
			.discountType(coupon.getDiscountType())
			.maxDiscountAmount(coupon.getMaxDiscountAmount())
			.minPurchaseAmount(coupon.getMinPurchaseAmount())
			.startedAt(coupon.getStartedAt())
			.expirationAt(coupon.getExpirationAt())
			.build();
	}
}
