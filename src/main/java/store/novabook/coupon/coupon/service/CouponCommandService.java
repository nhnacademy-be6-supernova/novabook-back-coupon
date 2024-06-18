package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;

public interface CouponCommandService {

	CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest);

	CreateCouponResponse saveBookCoupon(CreateBookCouponRequest createBookCouponRequest);

	CreateCouponResponse saveCategoryCoupon(CreateCategoryCouponRequest createCategoryCouponRequest);
}
