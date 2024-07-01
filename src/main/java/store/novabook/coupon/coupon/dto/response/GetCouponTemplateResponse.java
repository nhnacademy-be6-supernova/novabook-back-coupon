package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

/**
 * {@code GetCouponTemplateResponse} 레코드는 쿠폰 템플릿 조회 응답을 나타냅니다.
 *
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
public record GetCouponTemplateResponse(
	Long id,
	CouponType type,
	String name,
	long discountAmount,
	DiscountType discountType,
	long maxDiscountAmount,
	long minPurchaseAmount,
	LocalDateTime startedAt,
	LocalDateTime expirationAt,
	int usePeriod) {

	/**
	 * 주어진 쿠폰 템플릿 엔티티로부터 {@code GetCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param couponTemplate 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code GetCouponTemplateResponse} 객체
	 */
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

	/**
	 * 주어진 쿠폰 엔티티로부터 {@code GetCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param coupon 쿠폰 엔티티
	 * @return 생성된 {@code GetCouponTemplateResponse} 객체
	 */
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
