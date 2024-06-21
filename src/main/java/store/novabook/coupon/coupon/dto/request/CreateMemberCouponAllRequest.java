package store.novabook.coupon.coupon.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMemberCouponAllRequest(
	@NotNull(message = "회원 아이디는 필수 입력 항목입니다.") List<Long> memberIdList,
	@NotBlank(message = "쿠폰 코드는 필수 입력 항목입니다 ") String couponCode,
	@NotNull(message = "쿠폰만료일은 필수 입력 항목입니다 ") LocalDateTime expirationAt) {
}
