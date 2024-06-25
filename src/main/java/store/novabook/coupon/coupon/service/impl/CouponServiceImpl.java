package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {
	private final CouponRepository couponRepository;
	private final CouponTemplateRepository couponTemplateRepository;

	private static void validateExpiration(CouponTemplate couponTemplate) {
		if (couponTemplate.getExpirationAt().isBefore(LocalDateTime.now()) || couponTemplate.getStartedAt()
			.isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON_CODE);
		}
	}

	@Override
	public void updateStatusToUsed(Long id) {
		Coupon coupon = couponRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_NOT_FOUND));
		if (coupon.getStatus() == CouponStatus.USED) {
			throw new BadRequestException(ErrorCode.ALREADY_USED_COUPON);
		}
		coupon.updateStatus(CouponStatus.USED);
		coupon.updateUsedAt(LocalDateTime.now());
		couponRepository.save(coupon);
	}

	@Override
	public CreateCouponResponse create(CreateCouponRequest request) {
		CouponTemplate couponTemplate = couponTemplateRepository.findById(request.couponTemplateId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_TEMPLATE_NOT_FOUND));

		validateExpiration(couponTemplate);

		Coupon coupon = Coupon.of(couponTemplate, CouponStatus.UNUSED,
			couponTemplate.getStartedAt().plusHours(couponTemplate.getUsePeriod()));
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public GetCouponResponse findAllById(GetCouponAllRequest request) {
		return null;
	}
}
