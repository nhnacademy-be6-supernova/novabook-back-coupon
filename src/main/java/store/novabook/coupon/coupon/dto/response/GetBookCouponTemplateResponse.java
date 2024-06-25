package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@Builder
public record GetBookCouponTemplateResponse(Long bookId, Long id, CouponType type, String name, long discountAmount,
											DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
											LocalDateTime startedAt, LocalDateTime expirationAt, int usePeriod) {
	public static GetBookCouponTemplateResponse fromEntity(BookCouponTemplate bookCouponTemplate) {
		CouponTemplate couponTemplate = bookCouponTemplate.getCouponTemplate();
		return GetBookCouponTemplateResponse.builder()
			.bookId(bookCouponTemplate.getBookId())
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
}
