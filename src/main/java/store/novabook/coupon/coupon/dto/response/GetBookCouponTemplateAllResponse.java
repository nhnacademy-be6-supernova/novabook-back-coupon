package store.novabook.coupon.coupon.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;

/**
 * {@code GetBookCouponTemplateAllResponse} 레코드는 모든 도서 쿠폰 템플릿 조회 응답을 나타냅니다.
 *
 * @param responseList 도서 쿠폰 템플릿 응답 리스트
 */
@Builder
public record GetBookCouponTemplateAllResponse(List<GetBookCouponTemplateResponse> responseList) {

	/**
	 * 주어진 도서 쿠폰 템플릿 리스트로부터 {@code GetBookCouponTemplateAllResponse} 객체를 생성합니다.
	 *
	 * @param bookCouponTemplateList 도서 쿠폰 템플릿 리스트
	 * @return 생성된 {@code GetBookCouponTemplateAllResponse} 객체
	 */
	public static GetBookCouponTemplateAllResponse fromEntity(List<BookCouponTemplate> bookCouponTemplateList) {
		List<GetBookCouponTemplateResponse> couponResponseList = bookCouponTemplateList.stream()
			.map(GetBookCouponTemplateResponse::fromEntity)
			.toList();

		return GetBookCouponTemplateAllResponse.builder().responseList(couponResponseList).build();
	}
}
