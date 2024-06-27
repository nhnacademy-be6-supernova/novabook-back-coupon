package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@Builder
public record GetCouponTemplateResponse(Long id, CouponType type, String name, long discountAmount,
										DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
										LocalDateTime startedAt, LocalDateTime expirationAt, int usePeriod) {
	public static GetCouponTemplateResponse fromEntity(CouponTemplate couponTemplate) {
		return GetCouponTemplateResponse.builder()
			.id(couponTemplate.getId())
			.type(couponTemplate.getType())
			.name(couponTemplate.getName())
			.discountAmount(couponTemplate.getDiscountAmount())
			.discountType(couponTemplate.getDiscountType())
			.maxDiscountAmount(couponTemplate.getMaxDiscountAmount())
			.minPurchaseAmount(couponTemplate.getMinPurchaseAmount())
			.startedAt(couponTemplate.getStartedAt())
			.expirationAt(couponTemplate.getExpirationAt())
			.usePeriod(couponTemplate.getUsePeriod())
			.build();
	}

	public static GetCouponTemplateResponse fromEntity(Coupon coupon) {
		return GetCouponTemplateResponse.builder()
			.id(coupon.getCouponTemplate().getId())
			.type(coupon.getCouponTemplate().getType())
			.name(coupon.getCouponTemplate().getName())
			.discountAmount(coupon.getCouponTemplate().getDiscountAmount())
			.discountType(coupon.getCouponTemplate().getDiscountType())
			.maxDiscountAmount(coupon.getCouponTemplate().getMaxDiscountAmount())
			.minPurchaseAmount(coupon.getCouponTemplate().getMinPurchaseAmount())
			.startedAt(coupon.getCouponTemplate().getStartedAt())
			.expirationAt(coupon.getCouponTemplate().getExpirationAt())
			.usePeriod(coupon.getCouponTemplate().getUsePeriod())
			.build();
	}
}
