package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.messaging.dto.OrderSagaMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;

/**
 * 쿠폰 메시지를 전송하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class CouponSender {

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

	public void sendToApplyCouponQueue(OrderSagaMessage orderSagaMessage) {
		rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.api2-producer-routing-key", orderSagaMessage);
	}

	public void sendToCompensateApplyCouponQueue(OrderSagaMessage orderSagaMessage) {
		rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.orders.saga.dead.routing.key", orderSagaMessage);
	}

}
