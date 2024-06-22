package store.novabook.coupon.coupon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly);

	Page<GetMemberCouponResponse> getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status,
		Pageable pageable);

	void updateMemberCouponStatus(Long memberId, String memberCouponId, PutMemberCouponRequest request);

	CreateMemberCouponAllResponse saveMemberCouponAll(CreateMemberCouponAllRequest request);

}
