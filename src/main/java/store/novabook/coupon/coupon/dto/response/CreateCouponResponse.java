package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;

/**
 * {@code CreateCouponResponse} 레코드는 쿠폰 생성 응답을 나타냅니다.
 *
 * @param id 생성된 쿠폰의 ID
 */
@Builder
public record CreateCouponResponse(Long id) {

	/**
	 * 주어진 쿠폰 엔티티로부터 {@code CreateCouponResponse} 객체를 생성합니다.
	 *
	 * @param coupon 쿠폰 엔티티
	 * @return 생성된 {@code CreateCouponResponse} 객체
	 */
	public static CreateCouponResponse fromEntity(Coupon coupon) {
		return CreateCouponResponse.builder().id(coupon.getId()).build();
	}
}
