package store.novabook.coupon.common.messaging.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 쿠폰 등록 메시지 DTO 클래스.
 */
@Builder
public record RegisterCouponMessage(@NotNull Long memberId, @NotNull Long couponId) {
}
