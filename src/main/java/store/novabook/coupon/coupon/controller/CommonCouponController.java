package store.novabook.coupon.coupon.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.service.CommonCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/coupons")
public class CommonCouponController {

	private final CommonCouponService commonCouponService;

}
