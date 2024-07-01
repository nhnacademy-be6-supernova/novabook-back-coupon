package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;

/**
 * {@code CouponTemplateService} 인터페이스는 쿠폰 템플릿에 대한 서비스 작업을 정의합니다.
 */
public interface CouponTemplateService {

	/**
	 * 주어진 쿠폰 타입과 유효성 여부에 따라 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param type     쿠폰 타입
	 * @param isValid  쿠폰의 유효성 여부
	 * @param pageable 페이지 정보
	 * @return 쿠폰 타입과 유효성 여부에 따른 쿠폰 템플릿의 페이지
	 */
	Page<GetCouponTemplateResponse> findByTypeAndValid(CouponType type, boolean isValid, Pageable pageable);

	/**
	 * 모든 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 쿠폰 템플릿의 페이지
	 */
	Page<GetCouponTemplateResponse> findAll(Pageable pageable);

	/**
	 * 새로운 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 쿠폰 템플릿 생성 요청
	 * @return 생성된 쿠폰 템플릿의 응답
	 */
	CreateCouponTemplateResponse create(CreateCouponTemplateRequest request);
}
