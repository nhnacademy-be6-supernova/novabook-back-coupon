package store.novabook.coupon.coupon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponSearchController {

	private final CouponService couponService;

	@GetMapping("/book/{bookId}")
	public ResponseEntity<GetCouponBookAllResponse> getCouponBook(@PathVariable Long bookId) {
		GetCouponBookAllResponse couponBookAllResponse = couponService.getCouponBook(bookId);
		return ResponseEntity.ok(couponBookAllResponse);
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<GetCouponCategoryAllResponse> getCouponCategory(@PathVariable Long categoryId) {
		GetCouponCategoryAllResponse couponCategoryResponse = couponService.getCouponCategory(categoryId);
		return ResponseEntity.ok(couponCategoryResponse);
	}
}
