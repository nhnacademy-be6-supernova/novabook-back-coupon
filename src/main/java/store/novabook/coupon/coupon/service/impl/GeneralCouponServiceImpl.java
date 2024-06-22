package store.novabook.coupon.coupon.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.GeneralCouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralCouponServiceImpl implements GeneralCouponService {

	private final CouponCodeGenerator codeGenerator;
	private final CouponRepository couponRepository;

	private static void validGeneralCouponType(CreateCouponRequest createCouponRequest) {
		if (createCouponRequest.couponType() == CouponType.CATEGORY
			|| createCouponRequest.couponType() == CouponType.BOOK) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON_TYPE);
		}
	}

	@Override
	public CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest) {
		validGeneralCouponType(createCouponRequest);
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(createCouponRequest.couponType()),
			createCouponRequest);
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponResponse> getCouponGeneralAll(Pageable pageable) {
		Page<Coupon> couponList = couponRepository.findAllByCodeIsNotLikeAndCodeIsNotLike(CouponType.BOOK.getPrefix(),
			CouponType.CATEGORY.getPrefix(), pageable);
		return couponList.map(GetCouponResponse::fromEntity);
	}
}
