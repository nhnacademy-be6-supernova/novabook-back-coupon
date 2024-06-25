package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.repository.CategoryCouponTemplateRepository;
import store.novabook.coupon.coupon.service.CategoryCouponTemplateService;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCouponTemplateServiceImpl implements CategoryCouponTemplateService {

	private final CategoryCouponTemplateRepository categoryCouponTemplateRepository;
	private final StoreAdapter storeAdapter;

	@Transactional(readOnly = true)
	@Override
	public Page<GetCategoryCouponTemplateResponse> findAll(Pageable pageable) {
		return categoryCouponTemplateRepository.findAll(pageable).map(GetCategoryCouponTemplateResponse::fromEntity);
	}

	@Override
	public CreateCouponTemplateResponse create(CreateCategoryCouponTemplateRequest request) {
		CategoryCouponTemplate categoryCouponTemplate = CategoryCouponTemplate.of(request);
		CategoryCouponTemplate saved = categoryCouponTemplateRepository.save(categoryCouponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public GetCategoryCouponTemplateAllResponse findAllByCategoryId(Long categoryId, boolean isValid) {
		if (isValid) {
			List<CategoryCouponTemplate> templateList = categoryCouponTemplateRepository.findAllByCategoryIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				categoryId, LocalDateTime.now(), LocalDateTime.now());
			return GetCategoryCouponTemplateAllResponse.fromEntity(templateList);
		}

		List<CategoryCouponTemplate> templateList = categoryCouponTemplateRepository.findAllByCategoryId(categoryId);
		return GetCategoryCouponTemplateAllResponse.fromEntity(templateList);
	}
}
