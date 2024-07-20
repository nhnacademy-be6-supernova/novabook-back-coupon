package store.novabook.coupon.common.messaging.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 사가 메시지를 담는 클래스입니다.
 */
@Setter
@Getter
@Builder
@JsonDeserialize(builder = OrderSagaMessage.OrderSagaMessageBuilder.class)
public class OrderSagaMessage {
	/**
	 * 순수 금액
	 */
	long bookAmount;

	/**
	 * 총 결제금액
	 */
	long calculateTotalAmount;

	/**
	 * 쿠폰 사용 금액
	 */
	long couponAmount;

	/**
	 * 적립 금액
	 */
	long earnPointAmount;

	/**
	 * 적립금 사용 여부
	 */
	boolean noEarnPoint;

	/**
	 * 포인트 사용 여부
	 */
	boolean noUsePoint;

	/**
	 * 쿠폰 사용 여부
	 */
	boolean noUseCoupon;

	/**
	 * 주문 상태
	 */
	String status;

	/**
	 * 결제 요청 정보
	 */
	PaymentRequest paymentRequest;

	/**
	 * {@link OrderSagaMessage} 빌더를 위한 정적 내부 클래스입니다.
	 */
	@JsonPOJOBuilder(withPrefix = "")
	public static class OrderSagaMessageBuilder {
	}
}
