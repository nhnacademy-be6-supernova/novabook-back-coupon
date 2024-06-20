package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.service.GeneralCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons/general")
public class GeneralCouponController {

	private final GeneralCouponService generalCouponService;

	@PostMapping
	public ResponseEntity<CreateCouponResponse> saveGeneralCoupon(@Valid @RequestBody CreateCouponRequest request) {
		CreateCouponResponse response = generalCouponService.saveGeneralCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponResponse>> getCouponGeneralAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<GetCouponResponse> coupons = generalCouponService.getCouponGeneralAll(pageable);
		return ResponseEntity.ok(coupons);
	}
}