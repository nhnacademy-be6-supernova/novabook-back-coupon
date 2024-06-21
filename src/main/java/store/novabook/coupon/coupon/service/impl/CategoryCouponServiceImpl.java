package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.service.CategoryCouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryCouponServiceImpl implements CategoryCouponService {

	private final CouponCodeGenerator codeGenerator;
	private final CouponRepository couponRepository;
	private final CategoryCouponRepository categoryCouponRepository;

	@Override
	public CreateCouponResponse saveCategoryCoupon(CreateCouponCategoryRequest createCouponCategoryRequest) {
		Coupon coupon = Coupon.of(codeGenerator.generateUniqueCode(CouponType.CATEGORY), createCouponCategoryRequest);
		Coupon saved = couponRepository.save(coupon);
		categoryCouponRepository.save(CategoryCoupon.of(saved, createCouponCategoryRequest.categoryId()));
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponCategoryResponse> getCouponCategoryAll(Pageable pageable) {
		Page<CategoryCoupon> categoryCouponList = categoryCouponRepository.findAll(pageable);
		return categoryCouponList.map(GetCouponCategoryResponse::fromEntity);
	}

	// 만료시간이 현재시간보다 후인 쿠폰들만 가져온다.
	@Override
	@Transactional(readOnly = true)
	public GetCouponCategoryAllResponse getCouponCategory(Long categoryId) {
		List<CategoryCoupon> categoryCouponList = Optional.ofNullable(
				categoryCouponRepository.findAllByCategoryIdAndCouponExpirationAtAfterAndCouponStartedAtBefore(categoryId,
					LocalDateTime.now(), LocalDateTime.now()))
			.filter(list -> !list.isEmpty())
			.orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_COUPON_NOT_FOUND));
		return GetCouponCategoryAllResponse.fromEntity(categoryCouponList);
	}
}
