package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;

public interface CategoryCouponService {
	CreateCouponResponse saveCategoryCoupon(CreateCouponCategoryRequest createCouponCategoryRequest);

	@Transactional(readOnly = true)
	Page<GetCouponCategoryResponse> getCouponCategoryAll(Pageable pageable);

	@Transactional(readOnly = true)
	GetCouponCategoryAllResponse getCouponCategory(Long categoryId);
}
