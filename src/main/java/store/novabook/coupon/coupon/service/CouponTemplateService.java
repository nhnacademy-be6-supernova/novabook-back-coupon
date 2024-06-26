package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponType;

public interface CouponTemplateService {

	Page<GetCouponTemplateResponse> findByTypeAndValid(CouponType type, boolean isValid, Pageable pageable);

	Page<GetCouponTemplateResponse> findAll(Pageable pageable);

	CreateCouponTemplateResponse create(CreateCouponTemplateRequest request);
}
