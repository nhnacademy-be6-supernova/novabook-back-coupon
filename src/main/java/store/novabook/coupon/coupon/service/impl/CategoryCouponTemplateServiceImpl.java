package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.repository.CategoryCouponTemplateRepository;
import store.novabook.coupon.coupon.service.CategoryCouponTemplateService;

/**
 * {@code CategoryCouponTemplateServiceImpl} 클래스는 카테고리 쿠폰 템플릿에 대한 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCouponTemplateServiceImpl implements CategoryCouponTemplateService {

	private final CategoryCouponTemplateRepository categoryCouponTemplateRepository;

	/**
	 * 모든 카테고리 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 카테고리 쿠폰 템플릿의 페이지
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<GetCategoryCouponTemplateResponse> findAll(Pageable pageable) {
		return categoryCouponTemplateRepository.findAll(pageable).map(GetCategoryCouponTemplateResponse::fromEntity);
	}

	/**
	 * 새로운 카테고리 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 카테고리 쿠폰 템플릿의 응답
	 */
	@Override
	public CreateCouponTemplateResponse create(CreateCategoryCouponTemplateRequest request) {
		CategoryCouponTemplate categoryCouponTemplate = CategoryCouponTemplate.of(request);
		CategoryCouponTemplate saved = categoryCouponTemplateRepository.save(categoryCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

	/**
	 * 주어진 카테고리 ID와 유효성 여부에 따라 모든 카테고리 쿠폰 템플릿을 조회합니다.
	 *
	 * @param categoryIdList 카테고리 ID
	 * @param isValid        유효성 여부
	 * @return 주어진 조건에 맞는 카테고리 쿠폰 템플릿의 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCategoryCouponTemplateAllResponse findAllByCategoryId(List<Long> categoryIdList, boolean isValid) {
		if (isValid) {
			List<CategoryCouponTemplate> templateList = categoryCouponTemplateRepository.findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				categoryIdList, LocalDateTime.now(), LocalDateTime.now());
			return GetCategoryCouponTemplateAllResponse.fromEntity(templateList);
		}

		List<CategoryCouponTemplate> templateList = categoryCouponTemplateRepository.findAllByCategoryIdIn(
			categoryIdList);
		return GetCategoryCouponTemplateAllResponse.fromEntity(templateList);
	}
}
