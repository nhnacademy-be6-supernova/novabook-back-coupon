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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;

@Tag(name = "Coupon Template API", description = "쿠폰 템플릿 API")
public interface CouponTemplateControllerDocs {

	@Operation(summary = "쿠폰 템플릿 타입별 조회", description = "쿠폰 타입과 유효성 여부로 쿠폰 템플릿을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "쿠폰 템플릿 조회 성공", content = @Content(schema = @Schema(implementation = GetCouponTemplateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 쿠폰 타입", content = @Content),
		@ApiResponse(responseCode = "404", description = "쿠폰 템플릿을 찾을 수 없음", content = @Content)})
	ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAllByType(
		@Parameter(description = "조회할 쿠폰 타입", required = true) @RequestParam CouponType type,
		@Parameter(description = "쿠폰 템플릿의 유효성 여부", required = false) @RequestParam(defaultValue = "true") Boolean isValid,
		Pageable pageable);

	@Operation(summary = "모든 쿠폰 템플릿 조회", description = "페이지 정보를 이용해 모든 쿠폰 템플릿을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "쿠폰 템플릿 조회 성공", content = @Content(schema = @Schema(implementation = GetCouponTemplateResponse.class)))})
	ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAll(Pageable pageable);

	@Operation(summary = "새로운 쿠폰 템플릿 생성", description = "새로운 쿠폰 템플릿을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "쿠폰 템플릿 생성 성공", content = @Content(schema = @Schema(implementation = CreateCouponTemplateResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)})
	ResponseEntity<CreateCouponTemplateResponse> createCouponTemplate(
		@Parameter(description = "생성할 쿠폰 템플릿 정보", required = true) @Valid @RequestBody CreateCouponTemplateRequest request);
}
