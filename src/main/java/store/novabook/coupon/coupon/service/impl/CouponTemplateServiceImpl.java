package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponTemplateService;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponTemplateServiceImpl implements CouponTemplateService {

	private final CouponTemplateRepository couponTemplateRepository;

	private static void validateType(CouponType type) {
		if (type == CouponType.BOOK || type == CouponType.CATEGORY) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON_TYPE);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponTemplateResponse> findByTypeAndValid(CouponType type, boolean isValid, Pageable pageable) {
		if (isValid) {
			return couponTemplateRepository.findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(type,
				LocalDateTime.now(), LocalDateTime.now(), pageable).map(GetCouponTemplateResponse::fromEntity);
		}
		return couponTemplateRepository.findAllByType(type, pageable).map(GetCouponTemplateResponse::fromEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponTemplateResponse> findAll(Pageable pageable) {
		return couponTemplateRepository.findAll(pageable).map(GetCouponTemplateResponse::fromEntity);
	}

	@Override
	public CreateCouponTemplateResponse create(CreateCouponTemplateRequest request) {
		validateType(request.type());
		CouponTemplate couponTemplate = CouponTemplate.of(request);
		CouponTemplate saved = couponTemplateRepository.save(couponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}
}
