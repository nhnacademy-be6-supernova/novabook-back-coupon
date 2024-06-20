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
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.service.BookCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons/book")
public class BookCouponController {

	private final BookCouponService bookCouponService;

	@PostMapping
	public ResponseEntity<CreateCouponResponse> saveBookCoupon(@Valid @RequestBody CreateCouponBookRequest request) {
		CreateCouponResponse response = bookCouponService.saveBookCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponBookResponse>> getCouponBookAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<GetCouponBookResponse> coupons = bookCouponService.getCouponBookAll(pageable);
		return ResponseEntity.ok(coupons);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<GetCouponBookAllResponse> getCouponBook(@PathVariable Long bookId) {
		GetCouponBookAllResponse couponBookAllResponse = bookCouponService.getCouponBook(bookId);
		return ResponseEntity.ok(couponBookAllResponse);
	}
}
