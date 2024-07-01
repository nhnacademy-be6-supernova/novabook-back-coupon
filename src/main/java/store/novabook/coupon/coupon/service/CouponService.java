package store.novabook.coupon.coupon.service;

import java.util.List;

import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

public interface CouponService {
	void updateStatusToUsed(Long id);

	CreateCouponResponse create(CreateCouponRequest request);

	GetCouponAllResponse findSufficientCouponAllById(GetCouponAllRequest request);

	GetCouponAllResponse findAllByIdAndStatus(List<Long> couponIdList, CouponStatus status);

	GetCouponAllResponse findAllById(List<Long> couponIdList);
}
