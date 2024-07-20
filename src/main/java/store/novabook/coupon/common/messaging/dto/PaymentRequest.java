package store.novabook.coupon.common.messaging.dto;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * 결제 요청 정보를 담는 클래스입니다.
 *
 * @param type        결제 유형
 * @param memberId    회원 ID
 * @param orderCode   주문 코드
 * @param orderId     주문 ID (UUID)
 * @param paymentInfo 결제 정보
 */
public record PaymentRequest(
	PaymentType type,
	Long memberId,
	String orderCode,
	@NotNull UUID orderId,
	Object paymentInfo
) implements Serializable {
}
