package store.novabook.coupon.common.adapter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 쿠폰 등록 요청 DTO입니다.
 * 이 클래스는 쿠폰을 등록할 때 필요한 요청 데이터를 캡슐화하는 데 사용됩니다.
 */
@Builder
public record RegisterCouponRequest(@NotNull Long couponId) {

}
