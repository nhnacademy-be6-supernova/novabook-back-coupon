package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;

/**
 * {@code BookCouponTemplateService} 인터페이스는 도서 쿠폰 템플릿에 대한 서비스 작업을 정의합니다.
 */
public interface BookCouponTemplateService {

	/**
	 * 모든 도서 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 도서 쿠폰 템플릿의 페이지
	 */
	Page<GetBookCouponTemplateResponse> findAll(Pageable pageable);

	/**
	 * 새로운 도서 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 도서 쿠폰 템플릿 생성 요청
	 * @return 생성된 도서 쿠폰 템플릿의 응답
	 */
	CreateCouponTemplateResponse create(CreateBookCouponTemplateRequest request);

	/**
	 * 주어진 도서 ID와 유효성 여부에 따라 모든 도서 쿠폰 템플릿을 조회합니다.
	 *
	 * @param bookId  도서 ID
	 * @param isValid 유효성 여부
	 * @return 주어진 조건에 맞는 도서 쿠폰 템플릿의 응답
	 */
	GetBookCouponTemplateAllResponse findAllByBookId(Long bookId, boolean isValid);
}
