package store.novabook.coupon.coupon.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;

@Tag(name = "CouponTemplate API", description = "쿠폰 템플릿 API")
public interface CouponTemplateControllerDocs {

	@Operation(summary = "쿠폰 타입별 템플릿 조회", description = "쿠폰 타입과 유효성 여부로 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
	})
	ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAllByType(
		@RequestParam CouponType type,
		@RequestParam(defaultValue = "true") Boolean isValid,
		Pageable pageable
	);

	@Operation(summary = "모든 쿠폰 템플릿 조회", description = "페이지 정보를 이용해 모든 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "모든 쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
	})
	ResponseEntity<Page<GetCouponTemplateResponse>> getCouponTemplateAll(Pageable pageable);

	@Operation(summary = "쿠폰 템플릿 생성", description = "새로운 쿠폰 템플릿을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "쿠폰 템플릿 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouponTemplateResponse.class))
	})
	ResponseEntity<CreateCouponTemplateResponse> createCouponTemplate(
		@Valid @RequestBody CreateCouponTemplateRequest request
	);
}
