package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.entity.CouponTemplate;

public interface CouponSchedulerService {
	void scheduleCouponJob(CouponTemplate couponTemplate);
}
