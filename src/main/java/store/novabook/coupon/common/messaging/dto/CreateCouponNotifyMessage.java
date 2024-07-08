package store.novabook.coupon.common.messaging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.coupon.coupon.entity.CouponType;

@Builder
public record CreateCouponNotifyMessage(@NotNull String uuid, @NotNull Long memberId, @NotNull List<Long> couponIdList,
										@NotNull CouponType couponType, @NotNull Long couponTemplateId) {
}
