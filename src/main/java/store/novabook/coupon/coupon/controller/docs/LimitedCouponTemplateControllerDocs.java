package store.novabook.coupon.coupon.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;

public interface LimitedCouponTemplateControllerDocs {

	@Operation(summary = "선착순 쿠폰 템플릿 조회", description = "유효한 모든 선착순 쿠폰 템플릿을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "선착순 쿠폰 템플릿 조회 성공", content = @Content(schema = @Schema(implementation = GetLimitedCouponTemplateResponse.class)))})
	ResponseEntity<Page<GetLimitedCouponTemplateResponse>> getLimitedCouponTemplateAll(
		@Parameter(description = "유효한 쿠폰 템플릿만 조회할지 여부", required = false) @RequestParam(defaultValue = "true") boolean isValid,
		Pageable pageable);

	@Operation(summary = "새로운 선착순 쿠폰 템플릿 생성", description = "새로운 선착순 쿠폰 템플릿을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "선착순 쿠폰 템플릿 생성 성공", content = @Content(schema = @Schema(implementation = CreateCouponTemplateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)})
	ResponseEntity<CreateCouponTemplateResponse> createCategoryCouponTemplate(
		@Parameter(description = "생성할 선착순 쿠폰 템플릿 정보", required = true) @Valid @RequestBody CreateLimitedCouponTemplateRequest request);
}
