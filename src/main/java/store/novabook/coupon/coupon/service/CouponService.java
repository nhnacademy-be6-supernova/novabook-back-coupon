package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;

public interface CouponService {
	void updateStatusToUsed(Long id);

	CreateCouponResponse create(CreateCouponRequest request);

	GetCouponResponse findAllById(GetCouponAllRequest request);
}
