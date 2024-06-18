package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import store.novabook.coupon.coupon.domain.DiscountType;

public record GetCouponResponse(String code, String name, long discountAmount, DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime startedAt,
								LocalDateTime expirationAt) {
}
