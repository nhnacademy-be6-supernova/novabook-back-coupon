package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;

public interface CategoryCouponTemplateService {
	CreateCouponTemplateResponse create(CreateCategoryCouponTemplateRequest request);

	GetCategoryCouponTemplateAllResponse findAllByCategoryId(Long categoryId, boolean isValid);

	Page<GetCategoryCouponTemplateResponse> findAll(Pageable pageable);
}
