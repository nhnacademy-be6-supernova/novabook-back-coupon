package store.novabook.coupon.common.messaging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.coupon.coupon.entity.CouponType;

/**
 * 쿠폰 생성 알림 메시지를 담는 클래스입니다.
 *
 * @param uuid             UUID
 * @param memberId         회원 ID
 * @param couponIdList     쿠폰 ID 목록
 * @param couponType       쿠폰 유형
 * @param couponTemplateId 쿠폰 템플릿 ID
 */
@Builder
public record CreateCouponNotifyMessage(
	@NotNull String uuid,
	@NotNull Long memberId,
	@NotNull List<Long> couponIdList,
	@NotNull CouponType couponType,
	@NotNull Long couponTemplateId
) {
}
