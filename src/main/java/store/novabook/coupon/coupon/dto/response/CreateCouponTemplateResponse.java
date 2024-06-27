package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;

@Builder
public record CreateCouponTemplateResponse(Long id) {
	public static CreateCouponTemplateResponse fromEntity(CouponTemplate couponTemplate) {
		return CreateCouponTemplateResponse.builder()
			.id(couponTemplate.getId())
			.build();
	}

	public static CreateCouponTemplateResponse fromEntity(BookCouponTemplate bookCouponTemplate) {
		return CreateCouponTemplateResponse.builder()
			.id(bookCouponTemplate.getId())
			.build();
	}

	public static CreateCouponTemplateResponse fromEntity(CategoryCouponTemplate categoryCouponTemplate) {
		return CreateCouponTemplateResponse.builder()
			.id(categoryCouponTemplate.getId())
			.build();
	}
}
