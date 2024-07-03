package store.novabook.coupon.common.messaging.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.coupon.coupon.entity.CouponType;

@Builder
public record CreateCouponMessage(@NotNull Long memberId, CouponType couponType, Long couponTemplateId) {
	public static CreateCouponMessage fromEntity(Long memberId, CouponType couponType, Long couponTemplateId) {
		return CreateCouponMessage.builder()
			.memberId(memberId)
			.couponType(couponType)
			.couponTemplateId(couponTemplateId)
			.build();
	}
}
