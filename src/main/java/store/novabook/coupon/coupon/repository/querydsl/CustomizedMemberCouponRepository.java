package store.novabook.coupon.coupon.repository.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;

public interface CustomizedMemberCouponRepository {
	List<GetMemberCouponCategoryResponse> findCategoryCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetMemberCouponBookResponse> findBookCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetMemberCouponResponse> findGeneralCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetMemberCouponResponse> findValidCouponsByStatus(Long memberId, String prefix, MemberCouponStatus status,
		LocalDateTime expirationAt, LocalDateTime startedAt);
}
