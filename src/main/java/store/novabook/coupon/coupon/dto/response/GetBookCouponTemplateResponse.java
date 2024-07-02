package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

/**
 * {@code GetBookCouponTemplateResponse} 레코드는 도서 쿠폰 템플릿 조회 응답을 나타냅니다.
 *
 * @param bookId            도서 ID
 * @param id                쿠폰 템플릿 ID
 * @param type              쿠폰 타입
 * @param name              쿠폰 이름
 * @param discountAmount    할인 금액
 * @param discountType      할인 유형
 * @param maxDiscountAmount 최대 할인 금액
 * @param minPurchaseAmount 최소 구매 금액
 * @param startedAt         쿠폰 정책 시작 날짜
 * @param expirationAt      쿠폰 정책 만료 날짜
 * @param usePeriod         사용 가능 기간
 */
@Builder
public record GetBookCouponTemplateResponse(Long bookId, Long id, CouponType type, String name, long discountAmount,
											DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
											LocalDateTime startedAt, LocalDateTime expirationAt, int usePeriod) {

	/**
	 * 주어진 도서 쿠폰 템플릿 엔티티로부터 {@code GetBookCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param bookCouponTemplate 도서 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code GetBookCouponTemplateResponse} 객체
	 */
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
