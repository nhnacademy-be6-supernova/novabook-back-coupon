package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.DiscountType;

@Builder
public record GetMemberCouponCategoryResponse(Long memberCouponId, Long categoryId, String code, String name,
											  long discountAmount, DiscountType discountType, long maxDiscountAmount,
											  long minPurchaseAmount, LocalDateTime createdAt,
											  LocalDateTime expirationAt) {
}
