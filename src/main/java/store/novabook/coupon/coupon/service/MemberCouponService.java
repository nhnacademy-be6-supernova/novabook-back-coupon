package store.novabook.coupon.coupon.service;

import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	GetMemberCouponResponse getMemberCouponAll(Long memberId, Boolean validOnly);
}
