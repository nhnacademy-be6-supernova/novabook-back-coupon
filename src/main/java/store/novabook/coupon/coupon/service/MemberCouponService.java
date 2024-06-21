package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly);

	GetMemberCouponAllResponse getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status, Pageable pageable);

	void updateMemberCouponStatus(Long memberId, PutMemberCouponRequest request);
}
