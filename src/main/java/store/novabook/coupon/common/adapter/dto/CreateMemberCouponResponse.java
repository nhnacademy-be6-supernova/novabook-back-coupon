package store.novabook.coupon.common.adapter.dto;

import lombok.Builder;

/**
 * 회원 쿠폰 생성에 대한 응답 DTO입니다.
 * 이 클래스는 회원 쿠폰이 생성될 때 응답 데이터를 캡슐화하는 데 사용됩니다.
 */
@Builder
public record CreateMemberCouponResponse(Long couponId) {
}
