package store.novabook.coupon.coupon.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GetCouponAllRequest(@NotNull(message = "쿠폰 번호는 필수 입력 항목입니다.") List<Long> couponIdList) {
}
