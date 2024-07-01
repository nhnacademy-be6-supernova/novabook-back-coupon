package store.novabook.coupon.coupon.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;

@Tag(name = "CategoryCoupon API", description = "카테고리 쿠폰 템플릿 API")
public interface CategoryCouponTemplateControllerDocs {

	@Operation(summary = "모든 카테고리 쿠폰 템플릿 조회", description = "페이지 정보를 이용해 모든 카테고리 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "카테고리 쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
	})
	ResponseEntity<Page<GetCategoryCouponTemplateResponse>> getCategoryCouponTemplateAll(Pageable pageable);

	@Operation(summary = "카테고리 ID로 쿠폰 템플릿 조회", description = "카테고리 ID를 이용해 특정 카테고리의 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "카테고리 쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCategoryCouponTemplateAllResponse.class))
	})
	ResponseEntity<GetCategoryCouponTemplateAllResponse> getCategoryCouponTemplateAllByCategoryId(
		@PathVariable Long categoryId, @RequestParam(defaultValue = "true") boolean isValid);

	@Operation(summary = "카테고리 쿠폰 템플릿 생성", description = "새로운 카테고리 쿠폰 템플릿을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "카테고리 쿠폰 템플릿 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouponTemplateResponse.class))
	})
	ResponseEntity<CreateCouponTemplateResponse> createCategoryCouponTemplate(
		@Valid @RequestBody CreateCategoryCouponTemplateRequest request);
}
