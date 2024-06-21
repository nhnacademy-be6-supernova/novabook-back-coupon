package store.novabook.coupon.coupon.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.domain.MemberCoupon;

@Builder
public record GetMemberCouponResponse(Long memberCouponId, String code, String name, long discountAmount,
									  DiscountType discountType, long maxDiscountAmount, long minPurchaseAmount,
									  LocalDateTime startedAt, LocalDateTime expirationAt, int usePeriod) {
	public static GetMemberCouponResponse fromEntity(MemberCoupon memberCoupon) {
		return GetMemberCouponResponse.builder()
			.memberCouponId(memberCoupon.getId())
			.code(memberCoupon.getCoupon().getCode())
			.name(memberCoupon.getCoupon().getName())
			.discountAmount(memberCoupon.getCoupon().getDiscountAmount())
			.discountType(memberCoupon.getCoupon().getDiscountType())
			.maxDiscountAmount(memberCoupon.getCoupon().getMaxDiscountAmount())
			.minPurchaseAmount(memberCoupon.getCoupon().getMinPurchaseAmount())
			.startedAt(memberCoupon.getCoupon().getStartedAt())
			.expirationAt(memberCoupon.getCoupon().getExpirationAt())
			.usePeriod(memberCoupon.getCoupon().getUsePeriod())
			.build();
	}

}
