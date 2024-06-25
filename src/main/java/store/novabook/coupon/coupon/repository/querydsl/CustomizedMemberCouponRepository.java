package store.novabook.coupon.coupon.repository.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
import store.novabook.coupon.coupon.entity.CouponStatus;

public interface CustomizedMemberCouponRepository {
	List<GetMemberCouponCategoryResponse> findCategoryCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetMemberCouponBookResponse> findBookCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetCouponResponse> findGeneralCouponsByMemberId(Long memberId, boolean validOnly);

	List<GetCouponResponse> findValidCouponsByStatus(Long memberId, String prefix, CouponStatus status,
		LocalDateTime expirationAt, LocalDateTime startedAt);
}
