package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.service.CouponTemplateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon-templates")
public class CouponTemplateController {
	private final CouponTemplateService couponTemplateService;

	@GetMapping(params = "type")
	public ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAllByType(@RequestParam CouponType type,
		@RequestParam(defaultValue = "true") Boolean isValid, Pageable pageable) {
		Page<GetCouponTemplateResponse> response = couponTemplateService.findByTypeAndValid(type, isValid, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAll(Pageable pageable) {
		Page<GetCouponTemplateResponse> response = couponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createCouponTemplate(
		@Valid @RequestBody CreateCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = couponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
