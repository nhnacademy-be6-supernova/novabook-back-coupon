package store.novabook.coupon.coupon.dto.request;

import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(String couponCode) {
}
