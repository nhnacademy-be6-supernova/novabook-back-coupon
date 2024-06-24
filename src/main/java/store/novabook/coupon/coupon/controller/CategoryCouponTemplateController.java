package store.novabook.coupon.coupon.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.service.CategoryCouponTemplateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category-coupon-templates")
public class CategoryCouponTemplateController {

	private final CategoryCouponTemplateService categoryCouponTemplateService;

	@GetMapping
	public ResponseEntity<Page<GetCategoryCouponTemplateResponse>> getCategoryCouponTemplateAll(Pageable pageable) {
		Page<GetCategoryCouponTemplateResponse> response = categoryCouponTemplateService.findAll(pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/category/{categoryId}")
	public ResponseEntity<GetCategoryCouponTemplateAllResponse> getCategoryCouponTemplateAllByCategoryId(
		@PathVariable Long categoryId, @RequestParam(defaultValue = "true") boolean isValid) {
		GetCategoryCouponTemplateAllResponse response = categoryCouponTemplateService.findAllByCategoryId(categoryId,
			isValid);
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<CreateCouponTemplateResponse> createCategoryCouponTemplate(
		@Valid @RequestBody CreateCategoryCouponTemplateRequest request) {
		CreateCouponTemplateResponse response = categoryCouponTemplateService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
