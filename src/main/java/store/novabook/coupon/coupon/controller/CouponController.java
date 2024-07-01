package store.novabook.coupon.coupon.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.controller.docs.CouponControllerDocs;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/coupons")
public class CouponController implements CouponControllerDocs {

	private final CouponService couponService;

	@PostMapping("/sufficient")
	public ResponseEntity<GetCouponAllResponse> getSufficientCouponAll(
		@Valid @RequestBody GetCouponAllRequest request) {
		GetCouponAllResponse response = couponService.findSufficientCouponAllById(request);
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

	// TODO: 쿠폰ID 리스트가 들어오면 status에 따라서 반환. ( 사용, 미사용내역)
	// TODO: 쿠폰ID 리스트가 들어오면 반환 ( 발급내역 )
	@GetMapping
	public ResponseEntity<GetCouponAllResponse> getCouponAll(@RequestParam List<Long> couponIdList,
		@RequestParam(required = false) CouponStatus status) {
		GetCouponAllResponse response;
		if (status != null) {
			response = couponService.findAllByIdAndStatus(couponIdList, status);
		} else {
			response = couponService.findAllById(couponIdList);
		}
		return ResponseEntity.ok(response);

	}
}
