package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;

/**
 * {@code CategoryCouponTemplateService} 인터페이스는 카테고리 쿠폰 템플릿에 대한 서비스 작업을 정의합니다.
 */
public interface LimitedCouponTemplateService {

	/**
	 * 새로운 카테고리 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 카테고리 쿠폰 템플릿의 응답
	 */
	CreateCouponTemplateResponse create(CreateLimitedCouponTemplateRequest request);

	/**
	 * 모든 카테고리 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 카테고리 쿠폰 템플릿의 페이지
	 */
	Page<GetLimitedCouponTemplateResponse> findAll(Pageable pageable);

	Page<GetLimitedCouponTemplateResponse> findAllWithValid(Pageable pageable);
}
