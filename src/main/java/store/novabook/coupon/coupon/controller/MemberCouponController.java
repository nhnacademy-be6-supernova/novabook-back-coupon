package store.novabook.coupon.coupon.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.service.MemberCouponService;

@RestController
@RequiredArgsConstructor
public class MemberCouponController {

	private final MemberCouponService memberCouponService;

	@PostMapping("/coupons/members/{memberId}")
	public ResponseEntity<CreateMemberCouponResponse> saveMemberCoupon(@PathVariable Long memberId,
		@Valid @RequestBody CreateMemberCouponRequest request) {
		CreateMemberCouponResponse memberCouponResponse = memberCouponService.saveMemberCoupon(memberId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(memberCouponResponse);
	}

}
