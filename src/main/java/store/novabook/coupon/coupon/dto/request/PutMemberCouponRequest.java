package store.novabook.coupon.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;

public record PutMemberCouponRequest(
	@NotNull(message = "쿠폰 아이디는 필수 입력 항목입니다.") Long memberCouponId,
	@NotNull(message = "쿠폰 상태는 필수 입력 항목입니다.") MemberCouponStatus status) {
}
