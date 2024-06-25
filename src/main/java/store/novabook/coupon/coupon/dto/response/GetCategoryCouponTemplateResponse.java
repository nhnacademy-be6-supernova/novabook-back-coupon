package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@Builder
public record GetCategoryCouponTemplateResponse(Long categoryId, Long id, CouponType type, String name,
												long discountAmount, DiscountType discountType, long maxDiscountAmount,
												long minPurchaseAmount, LocalDateTime startedAt,
												LocalDateTime expirationAt, int usePeriod) {
	public static GetCategoryCouponTemplateResponse fromEntity(CategoryCouponTemplate categoryCouponTemplate) {
		return GetCategoryCouponTemplateResponse.builder()
			.categoryId(categoryCouponTemplate.getCategoryId())
			.id(categoryCouponTemplate.getId())
			.type(categoryCouponTemplate.getCouponTemplate().getType())
			.name(categoryCouponTemplate.getCouponTemplate().getName())
			.discountAmount(categoryCouponTemplate.getCouponTemplate().getDiscountAmount())
			.discountType(categoryCouponTemplate.getCouponTemplate().getDiscountType())
			.maxDiscountAmount(categoryCouponTemplate.getCouponTemplate().getMaxDiscountAmount())
			.minPurchaseAmount(categoryCouponTemplate.getCouponTemplate().getMinPurchaseAmount())
			.startedAt(categoryCouponTemplate.getCouponTemplate().getStartedAt())
			.expirationAt(categoryCouponTemplate.getCouponTemplate().getExpirationAt())
			.usePeriod(categoryCouponTemplate.getCouponTemplate().getUsePeriod())
			.build();
	}
}
