package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

/**
 * {@code GetCategoryCouponTemplateResponse} 레코드는 카테고리 쿠폰 템플릿 조회 응답을 나타냅니다.
 *
 * @param categoryId        카테고리 ID
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
public record GetCategoryCouponTemplateResponse(Long categoryId, Long id, CouponType type, String name,
												long discountAmount, DiscountType discountType, long maxDiscountAmount,
												long minPurchaseAmount, LocalDateTime startedAt,
												LocalDateTime expirationAt, int usePeriod) {

	/**
	 * 주어진 카테고리 쿠폰 템플릿 엔티티로부터 {@code GetCategoryCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param categoryCouponTemplate 카테고리 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code GetCategoryCouponTemplateResponse} 객체
	 */
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
