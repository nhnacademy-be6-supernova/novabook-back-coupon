package store.novabook.coupon.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * {@code CreateCouponRequest} 레코드는 쿠폰 생성 요청을 나타냅니다.
 *
 * @param couponTemplateId 쿠폰 템플릿 ID
 */
@Builder
public record CreateCouponRequest(@NotNull(message = "쿠폰 템플릿 ID는 필수 입력 항목입니다.") Long couponTemplateId) {
}
