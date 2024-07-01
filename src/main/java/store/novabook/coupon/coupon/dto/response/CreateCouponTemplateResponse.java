package store.novabook.coupon.coupon.dto.response;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;

/**
 * {@code CreateCouponTemplateResponse} 레코드는 쿠폰 템플릿 생성 응답을 나타냅니다.
 *
 * @param id 생성된 쿠폰 템플릿의 ID
 */
@Builder
public record CreateCouponTemplateResponse(Long id) {

	/**
	 * 주어진 쿠폰 템플릿 엔티티로부터 {@code CreateCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param couponTemplate 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code CreateCouponTemplateResponse} 객체
	 */
	public static CreateCouponTemplateResponse fromEntity(CouponTemplate couponTemplate) {
		return CreateCouponTemplateResponse.builder().id(couponTemplate.getId()).build();
	}

	/**
	 * 주어진 도서 쿠폰 템플릿 엔티로부터 {@code CreateCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param bookCouponTemplate 도서 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code CreateCouponTemplateResponse} 객체
	 */
	public static CreateCouponTemplateResponse fromEntity(BookCouponTemplate bookCouponTemplate) {
		return CreateCouponTemplateResponse.builder().id(bookCouponTemplate.getId()).build();
	}

	/**
	 * 주어진 카테고리 쿠폰 템플릿 엔티티로부터 {@code CreateCouponTemplateResponse} 객체를 생성합니다.
	 *
	 * @param categoryCouponTemplate 카테고리 쿠폰 템플릿 엔티티
	 * @return 생성된 {@code CreateCouponTemplateResponse} 객체
	 */
	public static CreateCouponTemplateResponse fromEntity(CategoryCouponTemplate categoryCouponTemplate) {
		return CreateCouponTemplateResponse.builder().id(categoryCouponTemplate.getId()).build();
	}
}
