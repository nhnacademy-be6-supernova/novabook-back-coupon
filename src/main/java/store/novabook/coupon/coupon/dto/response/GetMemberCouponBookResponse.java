package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;

@Builder
public record GetMemberCouponBookResponse(Long memberCouponId, Long bookId, String code, String name,
										  long discountAmount,
										  DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
										  LocalDateTime startedAt, LocalDateTime expirationAt) {
	public static GetMemberCouponBookResponse fromEntity(Long id, BookCoupon bookCoupon) {
		Coupon coupon = bookCoupon.getCoupon();
		return GetMemberCouponBookResponse.builder()
			.memberCouponId(id)
			.bookId(bookCoupon.getBookId())
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
