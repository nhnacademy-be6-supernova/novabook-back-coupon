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

@Service
@RequiredArgsConstructor
public class LimitedCouponTemplateServiceImpl implements LimitedCouponTemplateService {

	public static final long MIN_QUANTITY = 1L;
	private final LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	@Override
	public CreateCouponTemplateResponse create(CreateLimitedCouponTemplateRequest request) {
		LimitedCouponTemplate limitedCouponTemplate = LimitedCouponTemplate.of(request);
		LimitedCouponTemplate saved = limitedCouponTemplateRepository.save(limitedCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

	@Override
	public Page<GetLimitedCouponTemplateResponse> findAll(Pageable pageable) {
		return limitedCouponTemplateRepository.findAll(pageable).map(GetLimitedCouponTemplateResponse::fromEntity);
	}

	@Override
	public Page<GetLimitedCouponTemplateResponse> findAllWithValid(Pageable pageable) {
		Page<LimitedCouponTemplate> templateList = limitedCouponTemplateRepository.findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
			LocalDateTime.now(), LocalDateTime.now(), MIN_QUANTITY, pageable);
		return templateList.map(GetLimitedCouponTemplateResponse::fromEntity);
	}
}
