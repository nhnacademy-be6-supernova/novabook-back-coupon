package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.domain.Coupon;

public interface CouponSchedulerService {
	void scheduleCouponJob(Coupon coupon);
}
