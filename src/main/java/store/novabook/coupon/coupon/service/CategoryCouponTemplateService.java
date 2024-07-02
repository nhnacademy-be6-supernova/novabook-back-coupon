package store.novabook.coupon.coupon.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;

/**
 * {@code CategoryCouponTemplateService} 인터페이스는 카테고리 쿠폰 템플릿에 대한 서비스 작업을 정의합니다.
 */
public interface CategoryCouponTemplateService {

	/**
	 * 새로운 카테고리 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 카테고리 쿠폰 템플릿의 응답
	 */
	CreateCouponTemplateResponse create(CreateCategoryCouponTemplateRequest request);

	/**
	 * 주어진 카테고리 ID와 유효성 여부에 따라 모든 카테고리 쿠폰 템플릿을 조회합니다.
	 *
	 * @param categoryId 카테고리 ID
	 * @param isValid    유효성 여부
	 * @return 주어진 조건에 맞는 카테고리 쿠폰 템플릿의 응답
	 */
	GetCategoryCouponTemplateAllResponse findAllByCategoryId(List<Long> categoryId, boolean isValid);

	/**
	 * 모든 카테고리 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 카테고리 쿠폰 템플릿의 페이지
	 */
	Page<GetCategoryCouponTemplateResponse> findAll(Pageable pageable);
}
