package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@Builder
public record GetCouponResponse(Long id, CouponType type, CouponStatus status, String name, long discountAmount,
								DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime createdAt,
								LocalDateTime expirationAt) {
	public static GetCouponResponse fromEntity(Coupon coupon) {
		return GetCouponResponse.builder()
			.id(coupon.getId())
			.status(coupon.getStatus())
			.type(coupon.getCouponTemplate().getType())
			.name(coupon.getCouponTemplate().getName())
			.discountAmount(coupon.getCouponTemplate().getDiscountAmount())
			.discountType(coupon.getCouponTemplate().getDiscountType())
			.maxDiscountAmount(coupon.getCouponTemplate().getMaxDiscountAmount())
			.minPurchaseAmount(coupon.getCouponTemplate().getMinPurchaseAmount())
			.createdAt(coupon.getCreatedAt())
			.expirationAt(coupon.getExpirationAt())
			.build();
	}

}
