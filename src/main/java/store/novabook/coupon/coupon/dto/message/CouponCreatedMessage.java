package store.novabook.coupon.coupon.dto.message;

/**
 * {@code CouponCreatedMessage} 레코드는 쿠폰 생성 메시지를 나타냅니다.
 * 이 메시지는 쿠폰 ID와 회원 ID를 포함합니다.
 *
 * @param couponId 생성된 쿠폰의 ID
 * @param memberId 쿠폰이 할당된 회원의 ID
 */
public record CouponCreatedMessage(Long couponId, Long memberId) {
}
