package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;

public interface BookCouponService {
	CreateCouponResponse saveBookCoupon(CreateCouponBookRequest createCouponBookRequest);

	@Transactional(readOnly = true)
	Page<GetCouponBookResponse> getCouponBookAll(Pageable pageable);

	@Transactional(readOnly = true)
	GetCouponBookAllResponse getCouponBook(Long bookId);
}
