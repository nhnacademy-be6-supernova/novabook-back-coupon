package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;

public interface BookCouponTemplateService {
	Page<GetBookCouponTemplateResponse> findAll(Pageable pageable);

	CreateCouponTemplateResponse create(CreateBookCouponTemplateRequest request);

	GetBookCouponTemplateResponse findAllByBookId(Long bookId, boolean isValid);
}
