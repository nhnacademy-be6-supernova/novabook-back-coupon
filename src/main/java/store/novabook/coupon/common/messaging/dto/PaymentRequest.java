package store.novabook.coupon.common.messaging.dto;

import java.io.Serializable;

import lombok.Builder;

/**
 * 결제 요청 정보를 담는 클래스입니다.
 *
 * @param type        결제 유형
 * @param memberId    회원 ID
 * @param orderCode   주문 코드
 * @param paymentInfo 결제 정보
 */

@Builder
public record PaymentRequest(
	PaymentType type,
	Long memberId,
	String orderCode,
	Object paymentInfo
) implements Serializable {
}
