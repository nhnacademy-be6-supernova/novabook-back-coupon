package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;

/**
 * {@code GetCategoryCouponTemplateAllResponse} 레코드는 모든 카테고리 쿠폰 템플릿 조회 응답을 나타냅니다.
 *
 * @param responseList 카테고리 쿠폰 템플릿 응답 리스트
 */
@Builder
public record GetCategoryCouponTemplateAllResponse(List<GetCategoryCouponTemplateResponse> responseList) {

	/**
	 * 주어진 카테고리 쿠폰 템플릿 리스트로부터 {@code GetCategoryCouponTemplateAllResponse} 객체를 생성합니다.
	 *
	 * @param couponTemplateList 카테고리 쿠폰 템플릿 리스트
	 * @return 생성된 {@code GetCategoryCouponTemplateAllResponse} 객체
	 */
	public static GetCategoryCouponTemplateAllResponse fromEntity(List<CategoryCouponTemplate> couponTemplateList) {
		List<GetCategoryCouponTemplateResponse> couponResponseList = couponTemplateList.stream()
			.map(GetCategoryCouponTemplateResponse::fromEntity)
			.toList();

		return GetCategoryCouponTemplateAllResponse.builder().responseList(couponResponseList).build();
	}
}
