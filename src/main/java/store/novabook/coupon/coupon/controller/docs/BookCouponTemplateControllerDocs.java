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
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;

@Tag(name = "BookCoupon API")
public interface BookCouponTemplateControllerDocs {

	@Operation(summary = "모든 도서 쿠폰 템플릿 조회", description = "페이지 정보를 이용해 모든 도서 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "도서 쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
	})
	ResponseEntity<Page<GetBookCouponTemplateResponse>> getBookCouponTemplateAll(Pageable pageable);

	@Operation(summary = "도서 쿠폰 템플릿 생성", description = "새로운 도서 쿠폰 템플릿을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "도서 쿠폰 템플릿 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouponTemplateResponse.class))
	})
	ResponseEntity<CreateCouponTemplateResponse> createBookCouponTemplate(
		@Valid @RequestBody CreateBookCouponTemplateRequest request);

	@Operation(summary = "특정 도서 쿠폰 템플릿 조회", description = "ID를 이용해 특정 도서의 쿠폰 템플릿을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "도서 쿠폰 템플릿 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetBookCouponTemplateAllResponse.class))
	})
	ResponseEntity<GetBookCouponTemplateAllResponse> getCouponTemplateByBookId(@PathVariable Long bookId,
		@RequestParam(defaultValue = "true") boolean isValid);
}
