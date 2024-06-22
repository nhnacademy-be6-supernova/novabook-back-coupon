package store.novabook.coupon.coupon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.service.CommonCouponService;

// 일반 쿠폰, 생일 쿠폰,
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/coupons/common")
public class CommonCouponController {

	private final CommonCouponService commonCouponService;

}
