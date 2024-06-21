package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.service.MemberCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class MemberCouponController {

	private final MemberCouponService memberCouponService;

	// 회원에게 쿠폰 등록하기
	@PostMapping("/members/{memberId}/coupons")
	public ResponseEntity<CreateMemberCouponResponse> saveMemberCoupon(@PathVariable Long memberId,
		@Valid @RequestBody CreateMemberCouponRequest request) {
		CreateMemberCouponResponse memberCouponResponse = memberCouponService.saveMemberCoupon(memberId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(memberCouponResponse);
	}

	@PostMapping("/members/coupons")
	public ResponseEntity<Void> saveMemberCoupon(@Valid @RequestBody CreateMemberCouponAllRequest request) {
		memberCouponService.saveMemberCouponAll(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	// 주문 페이지 - 적용가능 한지?  만료기간이 현재보다 빠른지에 따라 (bookId, CategoryId)가 포함해서 주면 프론트 서버에서 관리됨
	@GetMapping("/members/{memberId}")
	public ResponseEntity<GetMemberCouponByTypeResponse> getMemberCoupon(@PathVariable Long memberId,
		@RequestParam(defaultValue = "true") Boolean validOnly) {
		GetMemberCouponByTypeResponse memberCouponResponse = memberCouponService.getMemberCouponAllByValid(memberId,
			validOnly);
		return ResponseEntity.status(HttpStatus.CREATED).body(memberCouponResponse);
	}

	// 마이페이지 - 쿠폰함/쿠폰내역에서 사용. "미사용"/"사용" 에 따라 가져옴
	@GetMapping("/members/{memberId}/coupons")
	public ResponseEntity<Page<GetMemberCouponResponse>> getMemberCouponByStatus(@PathVariable Long memberId,
		@RequestParam MemberCouponStatus status, @PageableDefault(size = 5) Pageable pageable) {
		Page<GetMemberCouponResponse> memberCouponResponse = memberCouponService.getMemberCouponAllByStatus(memberId,
			status, pageable);
		return ResponseEntity.ok(memberCouponResponse);
	}

	// 쿠폰 사용 요청
	@PutMapping("/members/{memberId}/coupons/{memberCouponId}")
	public ResponseEntity<Void> updateMemberCoupon(@PathVariable Long memberId, @PathVariable String memberCouponId,
		@Valid @RequestBody PutMemberCouponRequest request) {
		memberCouponService.updateMemberCouponStatus(memberId, memberCouponId, request);
		return ResponseEntity.ok().build();
	}

}
