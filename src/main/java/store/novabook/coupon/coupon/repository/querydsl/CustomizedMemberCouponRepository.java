package store.novabook.coupon.coupon.repository.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;

public interface CustomizedMemberCouponRepository {
	List<MemberCategoryCouponDto> findCategoryCouponsByMemberId(Long memberId, boolean validOnly);

	List<MemberBookCouponDto> findBookCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetMemberCouponResponse> findValidCouponsByStatus(Long memberId, String prefix, MemberCouponStatus status,
		LocalDateTime expirationAt, LocalDateTime startedAt);
}
