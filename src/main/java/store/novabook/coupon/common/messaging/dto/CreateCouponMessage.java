package store.novabook.coupon.common.messaging.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.coupon.coupon.entity.CouponType;

@Builder
public record CreateCouponMessage(@NotNull Long memberId, @NotNull CouponType couponType, Long couponTemplateId) {
	public static CreateCouponMessage fromEntity(Long id, CouponType couponType, Long couponTemplateId) {
		return CreateCouponMessage.builder()
			.memberId(id)
			.couponType(couponType)
			.couponTemplateId(couponTemplateId)
			.build();
	}
}
