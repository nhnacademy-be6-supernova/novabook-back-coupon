package store.novabook.coupon.coupon.repository;

import java.util.List;

import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;

/**
 * {@code CustomCouponRepository} 인터페이스는 사용자 정의 쿠폰 조회 메서드를 정의합니다.
 */
public interface CustomCouponRepository {

	/**
	 * 주어진 조건에 맞는 유효한 쿠폰들을 조회합니다.
	 *
	 * @param request 쿠폰 조회 요청
	 * @return 유효한 쿠폰 응답 리스트
	 */
	List<GetCouponResponse> findSufficientCoupons(GetCouponAllRequest request);
}
