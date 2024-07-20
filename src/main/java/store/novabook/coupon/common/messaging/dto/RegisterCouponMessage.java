package store.novabook.coupon.common.messaging.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 쿠폰 등록 메시지를 담는 클래스입니다.
 *
 * @param memberId 회원 ID
 * @param couponId 쿠폰 ID
 */
@Builder
public record RegisterCouponMessage(@NotNull Long memberId, @NotNull Long couponId) {
}
