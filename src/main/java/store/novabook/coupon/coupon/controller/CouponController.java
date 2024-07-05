package store.novabook.coupon.coupon.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * {@code CouponController} 클래스는 쿠폰 API를 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/coupons")
public class CouponController implements CouponControllerDocs {

	private final CouponService couponService;

	/**
	 * 요청된 조건에 맞는 모든 유효한 쿠폰을 조회합니다.
	 *
	 * @param request 유효한 쿠폰 조회 요청
	 * @return 유효한 쿠폰의 응답
	 */
	@PostMapping("/sufficient")
	public ResponseEntity<GetCouponAllResponse> getSufficientCouponAll(
		@Valid @RequestBody GetCouponAllRequest request) {
		GetCouponAllResponse response = couponService.findSufficientCouponAllById(request);
		return ResponseEntity.ok(response);
	}

	/**
	 * 새로운 쿠폰을 생성합니다.
	 *
	 * @param request 쿠폰 생성 요청
	 * @return 생성된 쿠폰의 응답
	 */
	@PostMapping
	public ResponseEntity<CreateCouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
		CreateCouponResponse response = couponService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 쿠폰 ID를 이용해 쿠폰을 사용처리합니다.
	 *
	 * @param couponId 사용 처리할 쿠폰의 ID
	 * @return 응답 엔터티
	 */
	@PutMapping("/{couponId}")
	public ResponseEntity<Void> useCoupon(@PathVariable Long couponId) {
		couponService.updateStatusToUsed(couponId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 쿠폰 ID 리스트와 상태를 이용해 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 조회할 쿠폰 ID 리스트
	 * @param status       쿠폰 상태 (선택 사항)
	 * @return 조회된 쿠폰의 응답
	 */
	@GetMapping
	public ResponseEntity<Page<GetCouponResponse>> getCouponAllWithPageable(@RequestParam List<Long> couponIdList,
		@RequestParam(required = false) CouponStatus status, Pageable pageable) {
		Page<GetCouponResponse> response;
		if (status != null) {
			response = couponService.findAllByIdAndStatus(couponIdList, status, pageable);
		} else {
			response = couponService.findAllById(couponIdList, pageable);
		}
		return ResponseEntity.ok(response);
	}

	/**
	 * 쿠폰 ID 리스트로 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 조회할 쿠폰 ID 리스트
	 * @return 유효한 쿠폰의 응답
	 */
	@GetMapping("/is-valid")
	public ResponseEntity<GetCouponAllResponse> getCouponAllWithPageable(@RequestParam List<Long> couponIdList) {
		GetCouponAllResponse allValidById = couponService.findAllValidById(couponIdList);
		return ResponseEntity.ok(allValidById);
	}
}
