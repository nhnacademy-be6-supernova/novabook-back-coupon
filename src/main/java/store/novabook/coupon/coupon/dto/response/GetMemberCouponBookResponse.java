package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.DiscountType;

@Builder
public record GetMemberCouponBookResponse(Long memberCouponId, Long bookId, String code, String name,
										  long discountAmount, DiscountType discountType, long maxDiscountAmount,
										  long minPurchaseAmount, LocalDateTime createdAt, LocalDateTime expirationAt) {
}
