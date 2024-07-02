package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.Coupon;

/**
 * {@code GetCouponAllResponse} 레코드는 모든 쿠폰 조회 응답을 나타냅니다.
 *
 * @param couponResponseList 쿠폰 응답 리스트
 */
@Builder
public record GetCouponAllResponse(List<GetCouponResponse> couponResponseList) {

	/**
	 * 주어진 쿠폰 엔티티 리스트로부터 {@code GetCouponAllResponse} 객체를 생성합니다.
	 *
	 * @param couponList 쿠폰 엔티티 리스트
	 * @return 생성된 {@code GetCouponAllResponse} 객체
	 */
	public static GetCouponAllResponse fromEntity(List<Coupon> couponList) {
		return GetCouponAllResponse.builder()
			.couponResponseList(couponList.stream().map(GetCouponResponse::fromEntity).toList())
			.build();
	}
}
