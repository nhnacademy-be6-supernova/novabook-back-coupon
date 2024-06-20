package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CommonCouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class CommonCouponServiceImpl implements CommonCouponService {

	private final CouponRepository couponRepository;

	@Override
	public void updateCouponExpiration(UpdateCouponExpirationRequest request) {
		Coupon coupon = couponRepository.findById(request.code())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
		coupon.updateExprationAt(LocalDateTime.now());
		couponRepository.save(coupon);
	}

}
