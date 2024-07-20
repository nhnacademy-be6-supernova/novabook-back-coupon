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
import store.novabook.coupon.coupon.controller.docs.LimitedCouponTemplateControllerDocs;
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;
import store.novabook.coupon.coupon.service.LimitedCouponTemplateService;

/**
 * {@code LimitedCouponTemplateController} 클래스는 선착순 쿠폰 템플릿 API를 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/templates/limited")
public class LimitedCouponTemplateController implements LimitedCouponTemplateControllerDocs {

	private final LimitedCouponTemplateService limitedCouponTemplateService;

	/**
	 * 모든 카테고리 쿠폰 템플릿을 조회합니다.
	 *
	 * @param isValid 유효한 쿠폰 템플릿만 조회할지 여부
	 * @return 선착순 쿠폰 템플릿의 페이지 응답
	 */
	@GetMapping
	public ResponseEntity<Page<GetLimitedCouponTemplateResponse>> getLimitedCouponTemplateAll(
		@RequestParam(defaultValue = "true") boolean isValid, Pageable pageable) {
		Page<GetLimitedCouponTemplateResponse> response;

		if (isValid) {
			response = limitedCouponTemplateService.findAllWithValid(pageable);
		} else {
			response = limitedCouponTemplateService.findAll(pageable);
		}

		return ResponseEntity.ok(response);
	}

	/**
	 * 새로운 카테고리 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 카테고리 쿠폰 템플릿의 응답
	 */
	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createCategoryCouponTemplate(
		@Valid @RequestBody CreateLimitedCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = limitedCouponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
