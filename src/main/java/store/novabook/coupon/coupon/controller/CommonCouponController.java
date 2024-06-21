package store.novabook.coupon.coupon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.service.CommonCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CommonCouponController {

	private final CommonCouponService commonCouponService;

	// 쿠폰 강제종료
	@PutMapping("/expiration")
	public ResponseEntity<Void> updateCouponExpiration(@Valid @RequestBody UpdateCouponExpirationRequest request) {
		commonCouponService.updateCouponExpiration(request);
		return ResponseEntity.ok(null);
	}

}
