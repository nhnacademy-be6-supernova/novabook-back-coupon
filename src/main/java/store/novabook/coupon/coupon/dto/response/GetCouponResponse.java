package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

/**
 * {@code GetCouponResponse} 레코드는 쿠폰 조회 응답을 나타냅니다.
 *
 * @param id                쿠폰 ID
 * @param type              쿠폰 타입
 * @param status            쿠폰 상태
 * @param name              쿠폰 이름
 * @param discountAmount    할인 금액
 * @param discountType      할인 유형
 * @param maxDiscountAmount 최대 할인 금액
 * @param minPurchaseAmount 최소 구매 금액
 * @param createdAt         쿠폰 생성 날짜
 * @param expirationAt      쿠폰 만료 날짜
 */
@Builder
public record GetCouponResponse(
	Long id,
	CouponType type,
	CouponStatus status,
	String name,
	long discountAmount,
	DiscountType discountType,
	long maxDiscountAmount,
	long minPurchaseAmount,
	LocalDateTime createdAt,
	LocalDateTime expirationAt) {

	/**
	 * 주어진 쿠폰 엔티티로부터 {@code GetCouponResponse} 객체를 생성합니다.
	 *
	 * @param coupon 쿠폰 엔티티
	 * @return 생성된 {@code GetCouponResponse} 객체
	 */
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
