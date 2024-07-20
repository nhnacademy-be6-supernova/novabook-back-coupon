package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.LimitedCouponTemplate;
import store.novabook.coupon.coupon.repository.LimitedCouponTemplateRepository;
import store.novabook.coupon.coupon.service.LimitedCouponTemplateService;

/**
 * {@code LimitedCouponTemplateServiceImpl} 클래스는 제한된 쿠폰 템플릿을 관리하는 서비스 구현 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class LimitedCouponTemplateServiceImpl implements LimitedCouponTemplateService {

	public static final long MIN_QUANTITY = 1L;
	private final LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	/**
	 * 새로운 제한된 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 제한된 쿠폰 템플릿 생성 요청
	 * @return 생성된 제한된 쿠폰 템플릿의 응답
	 */
	@Override
	public CreateCouponTemplateResponse create(CreateLimitedCouponTemplateRequest request) {
		LimitedCouponTemplate limitedCouponTemplate = LimitedCouponTemplate.of(request);
		LimitedCouponTemplate saved = limitedCouponTemplateRepository.save(limitedCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

	/**
	 * 모든 제한된 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 제한된 쿠폰 템플릿의 페이지 응답
	 */
	@Override
	public Page<GetLimitedCouponTemplateResponse> findAll(Pageable pageable) {
		return limitedCouponTemplateRepository.findAll(pageable).map(GetLimitedCouponTemplateResponse::fromEntity);
	}

	/**
	 * 유효한 모든 제한된 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 유효한 제한된 쿠폰 템플릿의 페이지 응답
	 */
	@Override
	public Page<GetLimitedCouponTemplateResponse> findAllWithValid(Pageable pageable) {
		Page<LimitedCouponTemplate> templateList = limitedCouponTemplateRepository.findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
			LocalDateTime.now(), LocalDateTime.now(), MIN_QUANTITY, pageable);
		return templateList.map(GetLimitedCouponTemplateResponse::fromEntity);
	}
}
