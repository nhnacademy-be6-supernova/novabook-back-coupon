package store.novabook.coupon.coupon.repository;

import java.util.List;

import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;

public interface CustomCouponRepository {
	List<GetCouponResponse> findSufficientCoupons(GetCouponAllRequest request);
}
