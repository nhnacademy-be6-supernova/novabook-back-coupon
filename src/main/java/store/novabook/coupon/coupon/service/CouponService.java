package store.novabook.coupon.coupon.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

/**
 * {@code CouponService} 인터페이스는 쿠폰에 대한 서비스 작업을 정의합니다.
 */
public interface CouponService {

	/**
	 * 쿠폰의 상태를 '사용됨'으로 업데이트합니다.
	 *
	 * @param id 쿠폰 ID
	 */
	void updateStatusToUsed(Long id);

	/**
	 * 새로운 쿠폰을 생성합니다.
	 *
	 * @param request 쿠폰 생성 요청
	 * @return 생성된 쿠폰의 응답
	 */
	CreateCouponResponse create(CreateCouponRequest request);

	/**
	 * 주어진 조건에 맞는 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param request 쿠폰 조회 요청
	 * @return 유효한 모든 쿠폰의 응답
	 */
	GetCouponAllResponse findSufficientCouponAllById(GetCouponAllRequest request);

	/**
	 * 주어진 쿠폰 ID 리스트와 상태에 따라 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @param status       쿠폰 상태
	 * @param pageable     페이징 정보
	 * @return 조회된 쿠폰의 응답
	 */
	@Transactional(readOnly = true)
	Page<GetCouponResponse> findAllByIdAndStatus(List<Long> couponIdList, CouponStatus status, Pageable pageable);

	/**
	 * 주어진 쿠폰 ID 리스트에 따라 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @param pageable     페이징 정보
	 * @return 조회된 쿠폰의 응답
	 */
	@Transactional(readOnly = true)
	Page<GetCouponResponse> findAllById(List<Long> couponIdList, Pageable pageable);

	/**
	 * 주어진 쿠폰 ID 리스트로 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @return 유효한 모든 쿠폰의 응답
	 */
	GetCouponAllResponse findAllValidById(List<Long> couponIdList);

	/**
	 * 회원 가입 메시지를 처리합니다.
	 *
	 * @param message 쿠폰 생성 메시지
	 * @return 유효한 모든 쿠폰의 응답
	 */
	CreateCouponResponse createByMessage(CreateCouponMessage message);
}
