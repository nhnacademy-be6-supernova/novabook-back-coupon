package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly);

	GetMemberCouponResponse getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status, Pageable pageable);
}
