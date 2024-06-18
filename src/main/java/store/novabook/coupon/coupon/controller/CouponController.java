package store.novabook.coupon.coupon.controller;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
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
	public ResponseEntity<CreateCouponResponse> saveBookCoupon(@Valid @RequestBody CreateCouponBookRequest request) {
		CreateCouponResponse response = couponService.saveBookCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/category")
	public ResponseEntity<CreateCouponResponse> saveCategoryCoupon(
		@Valid @RequestBody CreateCouponCategoryRequest request) {
		CreateCouponResponse response = couponService.saveCategoryCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/expiration")
	public ResponseEntity<Void> updateCouponExpiration(@Valid @RequestBody UpdateCouponExpirationRequest request) {
		couponService.updateCouponExpiration(request);
		return ResponseEntity.ok(null);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponResponse>> getCouponGeneralAll(@PageableDefault(size = 10) Pageable pageable) {
		Page<GetCouponResponse> coupons = couponService.getCouponGeneralAll(pageable);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponBookResponse>> getCouponBookAll(@PageableDefault(size = 10) Pageable pageable) {
		Page<GetCouponBookResponse> coupons = couponService.getCouponBookAll(pageable);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponCategoryResponse>> getCouponCategoryAll(
		@PageableDefault(size = 10) Pageable pageable) {
		Page<GetCouponCategoryResponse> coupons = couponService.getCouponCategryAll(pageable);
		return ResponseEntity.ok(coupons);
	}

}
