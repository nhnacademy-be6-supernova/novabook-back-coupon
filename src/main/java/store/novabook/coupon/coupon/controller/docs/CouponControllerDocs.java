package store.novabook.coupon.coupon.controller.docs;

import java.util.List;

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
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

@Tag(name = "Coupon API", description = "쿠폰 API")
public interface CouponControllerDocs {

	@Operation(summary = "유효한 모든 쿠폰 조회", description = "요청된 조건에 맞는 모든 유효한 쿠폰을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "유효한 쿠폰 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCouponAllResponse.class))
	})
	ResponseEntity<GetCouponAllResponse> getSufficientCouponAll(@Valid @RequestBody GetCouponAllRequest request);

	@Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "쿠폰 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouponResponse.class))
	})
	ResponseEntity<CreateCouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request);

	@Operation(summary = "쿠폰 사용", description = "쿠폰 ID를 이용해 쿠폰을 사용처리합니다.")
	@ApiResponse(responseCode = "200", description = "쿠폰 사용처리에 성공하였습니다.")
	ResponseEntity<Void> useCoupon(@PathVariable Long couponId);

	@Operation(summary = "쿠폰 ID 리스트로 쿠폰 조회", description = "쿠폰 ID 리스트와 상태를 이용해 쿠폰을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "쿠폰 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCouponAllResponse.class))
	})
	ResponseEntity<GetCouponAllResponse> getCouponAll(@RequestParam List<Long> couponIdList,
		@RequestParam(required = false) CouponStatus status);
}
