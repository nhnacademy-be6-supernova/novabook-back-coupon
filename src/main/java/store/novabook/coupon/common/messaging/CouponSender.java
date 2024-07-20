package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.messaging.dto.OrderSagaMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.common.messaging.dto.RequestPayCancelMessage;

/**
 * 쿠폰 메시지를 전송하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CouponSender {

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";
	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.couponOperation}")
	private String couponOperationExchange;

	@Value("${rabbitmq.routing.couponRegisterHighTraffic}")
	private String couponRegisterHighTrafficRoutingKey;

	@Value("${rabbitmq.routing.couponRegisterNormal}")
	private String couponRegisterNormalRoutingKey;

	/**
	 * 일반 큐로 쿠폰 등록 메시지를 전송합니다.
	 *
	 * @param message RegisterCouponMessage 객체
	 */
	public void sendToRegisterNormalQueue(RegisterCouponMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponRegisterNormalRoutingKey, message);
	}

	/**
	 * 주문 사가 메시지를 전송하여 쿠폰을 적용합니다.
	 *
	 * @param orderSagaMessage OrderSagaMessage 객체
	 */
	public void sendToApplyCouponQueue(OrderSagaMessage orderSagaMessage) {
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api2-producer-routing-key", orderSagaMessage);
	}

	/**
	 * 주문 사가 메시지를 전송하여 쿠폰 적용을 보상합니다.
	 *
	 * @param orderSagaMessage OrderSagaMessage 객체
	 */
	public void sendToCompensateApplyCouponQueue(OrderSagaMessage orderSagaMessage) {
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
	}

	/**
	 * 결제 취소 요청 메시지를 전송하여 쿠폰 상태를 변경합니다.
	 *
	 * @param requestPayCancelMessage RequestPayCancelMessage 객체
	 */
	public void sendToRequestPayCancelQueue(RequestPayCancelMessage requestPayCancelMessage) {
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key",
			requestPayCancelMessage);
	}
}
