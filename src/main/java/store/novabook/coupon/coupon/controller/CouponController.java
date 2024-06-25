package store.novabook.coupon.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

	private final CouponService couponService;

	@GetMapping
	public ResponseEntity<GetCouponAllResponse> getCouponAll(@Valid @RequestBody GetCouponAllRequest request) {
		GetCouponAllResponse response = couponService.findAllById(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<CreateCouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
		CreateCouponResponse response = couponService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{couponId}")
	public ResponseEntity<Void> useCoupon(@PathVariable Long couponId) {
		couponService.updateStatusToUsed(couponId);
		return ResponseEntity.ok().build();
	}
}
