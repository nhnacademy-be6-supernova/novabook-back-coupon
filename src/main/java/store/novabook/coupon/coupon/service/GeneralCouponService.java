package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;

public interface GeneralCouponService {
	CreateCouponResponse saveGeneralCoupon(CreateCouponRequest request);

	Page<GetCouponResponse> getCouponGeneralAll(Pageable pageable);
}
