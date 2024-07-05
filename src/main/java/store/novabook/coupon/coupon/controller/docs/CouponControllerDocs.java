package store.novabook.coupon.coupon.controller.docs;

import java.util.List;

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
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

/**
 * {@code CouponControllerDocs} 인터페이스는 쿠폰 API의 문서화를 제공합니다.
 */
@Tag(name = "Coupon API", description = "쿠폰 API")
public interface CouponControllerDocs {

	/**
	 * 요청된 조건에 맞는 모든 유효한 쿠폰을 조회합니다.
	 *
	 * @param request 유효한 쿠폰 조회 요청
	 * @return 유효한 쿠폰의 응답
	 */
	@Operation(summary = "유효한 모든 쿠폰 조회", description = "요청된 조건에 맞는 모든 유효한 쿠폰을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "유효한 쿠폰 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCouponAllResponse.class))})
	ResponseEntity<GetCouponAllResponse> getSufficientCouponAll(@Valid @RequestBody GetCouponAllRequest request);

	/**
	 * 새로운 쿠폰을 생성합니다.
	 *
	 * @param request 쿠폰 생성 요청
	 * @return 생성된 쿠폰의 응답
	 */
	@Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
	@ApiResponse(responseCode = "201", description = "쿠폰 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateCouponResponse.class))})
	ResponseEntity<CreateCouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest request);

	/**
	 * 쿠폰 ID를 이용해 쿠폰을 사용처리합니다.
	 *
	 * @param couponId 사용 처리할 쿠폰의 ID
	 * @return 응답 엔터티
	 */
	@Operation(summary = "쿠폰 사용", description = "쿠폰 ID를 이용해 쿠폰을 사용처리합니다.")
	@ApiResponse(responseCode = "200", description = "쿠폰 사용처리에 성공하였습니다.")
	ResponseEntity<Void> useCoupon(@PathVariable Long couponId);

	/**
	 * 쿠폰 ID 리스트와 상태를 이용해 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 조회할 쿠폰 ID 리스트
	 * @param status       쿠폰 상태 (선택 사항)
	 * @param pageable     페이징 정보
	 * @return 조회된 쿠폰의 응답
	 */
	@Operation(summary = "쿠폰 조회", description = "쿠폰 ID 리스트와 상태를 이용해 쿠폰을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "쿠폰 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCouponResponse.class))})
	ResponseEntity<Page<GetCouponResponse>> getCouponAllWithPageable(@RequestParam List<Long> couponIdList,
		@RequestParam(required = false) CouponStatus status, Pageable pageable);

	/**
	 * 쿠폰 ID 리스트로 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 조회할 쿠폰 ID 리스트
	 * @return 유효한 쿠폰의 응답
	 */
	@Operation(summary = "유효한 모든 쿠폰 조회", description = "쿠폰 ID 리스트로 유효한 모든 쿠폰을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "유효한 쿠폰 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetCouponAllResponse.class))})
	ResponseEntity<GetCouponAllResponse> getCouponAllWithPageable(@RequestParam List<Long> couponIdList);
}
