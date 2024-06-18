package store.novabook.coupon.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

	private final CouponService couponService;

	@PostMapping
	public ResponseEntity<CreateCouponResponse> saveGeneralCoupon(@Valid @RequestBody CreateCouponRequest request) {
		CreateCouponResponse response = couponService.saveGeneralCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/book")
	public ResponseEntity<CreateCouponResponse> saveBookCoupon(@Valid @RequestBody CreateBookCouponRequest request) {
		CreateCouponResponse response = couponService.saveBookCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}

	@PostMapping("/category")
	public ResponseEntity<CreateCouponResponse> saveCategoryCoupon(
		@Valid @RequestBody CreateCategoryCouponRequest request) {
		CreateCouponResponse response = couponService.saveCategoryCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/expiration")
	public ResponseEntity<Void> updateCouponExpiration(
		@Valid @RequestBody UpdateCouponExpirationRequest request) {
		couponService.updateCouponExpiration(request);
		return ResponseEntity.ok(null);
	}
}
