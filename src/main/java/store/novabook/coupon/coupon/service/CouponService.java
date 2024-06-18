package store.novabook.coupon.coupon.service;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;

import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;

public interface CouponService {

	CreateCouponResponse saveGeneralCoupon(CreateCouponRequest createCouponRequest);

	CreateCouponResponse saveBookCoupon(CreateCouponBookRequest createCouponBookRequest);

	CreateCouponResponse saveCategoryCoupon(CreateCouponCategoryRequest createCouponCategoryRequest);

	void updateCouponExpiration(UpdateCouponExpirationRequest request);

	Page<GetCouponResponse> getCouponGeneralAll(Pageable pageable);

	Page<GetCouponBookResponse> getCouponBookAll(Pageable pageable);

	Page<GetCouponCategoryResponse> getCouponCategryAll(Pageable pageable);
}
