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
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.coupon.controller.docs.CouponTemplateControllerDocs;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.service.CouponTemplateService;

/**
 * {@code CouponTemplateController} 클래스는 쿠폰 템플릿 API를 처리합니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/templates")
public class CouponTemplateController implements CouponTemplateControllerDocs {

	private final CouponTemplateService couponTemplateService;

	/**
	 * 쿠폰 타입과 유효성 여부로 쿠폰 템플릿을 조회합니다.
	 *
	 * @param type     쿠폰 타입
	 * @param isValid  유효성 여부
	 * @param pageable 페이지 정보
	 * @return 쿠폰 템플릿의 페이지 응답
	 */
	@GetMapping(params = "type")
	public ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAllByType(@RequestParam CouponType type,
		@RequestParam(defaultValue = "true") Boolean isValid, Pageable pageable) {
		Page<GetCouponTemplateResponse> response = couponTemplateService.findByTypeAndValid(type, isValid, pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * 페이지 정보를 이용해 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 쿠폰 템플릿의 페이지 응답
	 */
	@GetMapping
	public ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAll(Pageable pageable) {
		Page<GetCouponTemplateResponse> response = couponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * 새로운 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 쿠폰 템플릿 생성 요청
	 * @return 생성된 쿠폰 템플릿의 응답
	 */
	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createCouponTemplate(
		@Valid @RequestBody CreateCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = couponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
