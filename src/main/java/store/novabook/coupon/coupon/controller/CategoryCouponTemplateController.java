package store.novabook.coupon.coupon.controller;

import java.util.List;

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
import store.novabook.coupon.coupon.controller.docs.CategoryCouponTemplateControllerDocs;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.service.CategoryCouponTemplateService;

/**
 * {@code CategoryCouponTemplateController} 클래스는 카테고리 쿠폰 템플릿 API를 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon/templates/category")
public class CategoryCouponTemplateController implements CategoryCouponTemplateControllerDocs {

	private final CategoryCouponTemplateService categoryCouponTemplateService;

	/**
	 * 모든 카테고리 쿠폰 템플릿을 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 카테고리 쿠폰 템플릿의 페이지 응답
	 */
	@GetMapping
	public ResponseEntity<Page<GetCategoryCouponTemplateResponse>> getCategoryCouponTemplateAll(Pageable pageable) {
		Page<GetCategoryCouponTemplateResponse> response = categoryCouponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	/**
	 * 여러 카테고리 ID로 쿠폰 템플릿을 조회합니다.
	 *
	 * @param categoryIdList 조회할 카테고리의 ID 리스트
	 * @param isValid        유효성 여부
	 * @return 조회된 카테고리 쿠폰 템플릿의 응답
	 */
	@GetMapping(value = "/categories", params = {"categoryIdList"})
	public ResponseEntity<GetCategoryCouponTemplateAllResponse> getCategoryCouponTemplateAllByCategoryIdAll(
		@RequestParam List<Long> categoryIdList, @RequestParam(defaultValue = "true") boolean isValid) {
		GetCategoryCouponTemplateAllResponse response = categoryCouponTemplateService.findAllByCategoryId(
			categoryIdList, isValid);
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
		@Valid @RequestBody CreateCategoryCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = categoryCouponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
