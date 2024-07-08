package store.novabook.coupon.common.messaging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.coupon.coupon.entity.CouponType;

/**
 * 쿠폰 생성 메시지 DTO 클래스.
 */
@Builder
public record CreateCouponMessage(@NotNull Long memberId, @NotNull List<Long> couponIdList, CouponType couponType,
								  Long couponTemplateId) {
	/**
	 * 엔티티에서 CreateCouponMessage 객체를 생성합니다.
	 *
	 * @param memberId         회원 ID
	 * @param couponType       쿠폰 유형
	 * @param couponTemplateId 쿠폰 템플릿 ID
	 * @return 생성된 CreateCouponMessage 객체
	 */
	public static CreateCouponMessage fromEntity(Long memberId, List<Long> couponIdList, CouponType couponType,
		Long couponTemplateId) {
		return CreateCouponMessage.builder()
			.memberId(memberId)
			.couponIdList(couponIdList)
			.couponType(couponType)
			.couponTemplateId(couponTemplateId)
			.build();
	}

	public static CreateCouponMessage fromEntity(CreateCouponNotifyMessage message) {
		return CreateCouponMessage.builder()
			.memberId(message.memberId())
			.couponIdList(message.couponIdList())
			.couponType(message.couponType())
			.couponTemplateId(message.couponTemplateId())
			.build();
	}
}
