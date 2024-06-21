package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record GetCouponResponse(String code, String name, long discountAmount, DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime startedAt,
								LocalDateTime expirationAt, int usePeriod) {
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
			.usePeriod(coupon.getUsePeriod())
			.build();
	}

	public static GetCouponResponse fromEntity(MemberCoupon memberCoupon) {
		return GetCouponResponse.builder()
			.code(memberCoupon.getCoupon().getCode())
			.name(memberCoupon.getCoupon().getName())
			.discountAmount(memberCoupon.getCoupon().getDiscountAmount())
			.discountType(memberCoupon.getCoupon().getDiscountType())
			.maxDiscountAmount(memberCoupon.getCoupon().getMaxDiscountAmount())
			.minPurchaseAmount(memberCoupon.getCoupon().getMinPurchaseAmount())
			.startedAt(memberCoupon.getCoupon().getStartedAt())
			.expirationAt(memberCoupon.getCoupon().getExpirationAt())
			.usePeriod(memberCoupon.getCoupon().getUsePeriod())
			.build();
	}
}
