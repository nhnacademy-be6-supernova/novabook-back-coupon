package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.service.CategoryCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons/category")
public class CategoryCouponController {

	private final CategoryCouponService categoryCouponService;

	@PostMapping
	public ResponseEntity<CreateCouponResponse> saveCategoryCoupon(
		@Valid @RequestBody CreateCouponCategoryRequest request) {
		CreateCouponResponse response = categoryCouponService.saveCategoryCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponCategoryResponse>> getCouponCategoryAll(
		@PageableDefault(size = 5) Pageable pageable) {
		Page<GetCouponCategoryResponse> coupons = categoryCouponService.getCouponCategoryAll(pageable);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity<GetCouponCategoryAllResponse> getCouponCategory(@PathVariable Long categoryId) {
		GetCouponCategoryAllResponse couponCategoryResponse = categoryCouponService.getCouponCategory(categoryId);
		return ResponseEntity.ok(couponCategoryResponse);
	}
}
