package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;

public interface CouponService {

	CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest);

	CreateCouponResponse saveBookCoupon(CreateCouponBookRequest createCouponBookRequest);

	CreateCouponResponse saveCategoryCoupon(CreateCouponCategoryRequest createCouponCategoryRequest);

	void updateCouponExpiration(UpdateCouponExpirationRequest request);
}
