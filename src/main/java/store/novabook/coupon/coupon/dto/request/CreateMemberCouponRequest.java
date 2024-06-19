package store.novabook.coupon.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(
	@NotNull(message = "쿠폰 코드는 필수 입력 항목입니다.") @NotBlank(message = "쿠폰 코드는 필수 입력 항목입니다.") String couponCode) {
}
