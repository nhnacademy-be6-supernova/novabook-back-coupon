package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

public interface MemberCouponService {
	CreateCouponResponse saveMemberCoupon(Long memberId, CreateCouponRequest request);

	GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly);

	Page<GetCouponResponse> getMemberCouponAllByStatus(Long memberId, CouponStatus status,
		Pageable pageable);

	void updateMemberCouponStatus(Long memberId, String memberCouponId, PutMemberCouponRequest request);

	CreateMemberCouponAllResponse saveMemberCouponAll(CreateMemberCouponAllRequest request);

}
