package store.novabook.coupon.common.adapter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RegisterCouponRequest(@NotNull Long couponId) {
}
