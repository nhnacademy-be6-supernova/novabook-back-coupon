package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;

@Builder
public record GetCategoryCouponTemplateAllResponse(List<GetCategoryCouponTemplateResponse> responseList) {
	public static GetCategoryCouponTemplateAllResponse fromEntity(List<CategoryCouponTemplate> couponTemplateList) {
		List<GetCategoryCouponTemplateResponse> couponResponseList = couponTemplateList.stream()
			.map(GetCategoryCouponTemplateResponse::fromEntity)
			.toList();

		return GetCategoryCouponTemplateAllResponse.builder().responseList(couponResponseList).build();
	}
}
