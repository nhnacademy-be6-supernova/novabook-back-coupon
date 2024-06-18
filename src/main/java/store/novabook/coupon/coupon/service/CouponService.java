package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;

public interface CouponService {

	CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest);

	CreateCouponResponse saveBookCoupon(CreateBookCouponRequest createBookCouponRequest);

	CreateCouponResponse saveCategoryCoupon(CreateCategoryCouponRequest createCategoryCouponRequest);

	void updateCouponExpiration(UpdateCouponExpirationRequest request);
}
