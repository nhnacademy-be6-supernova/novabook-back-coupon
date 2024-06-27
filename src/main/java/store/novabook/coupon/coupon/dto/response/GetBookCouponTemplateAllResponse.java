package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;

@Builder
public record GetBookCouponTemplateAllResponse(List<GetBookCouponTemplateResponse> responseList) {
	public static GetBookCouponTemplateAllResponse fromEntity(List<BookCouponTemplate> bookCouponTemplateList) {
		List<GetBookCouponTemplateResponse> couponResponseList = bookCouponTemplateList.stream()
			.map(GetBookCouponTemplateResponse::fromEntity)
			.toList();

		return GetBookCouponTemplateAllResponse.builder().responseList(couponResponseList).build();
	}
}
