package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;

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

	public void sendToRegisterHighTrafficQueue(RegisterCouponMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponRegisterHighTrafficRoutingKey, message);
	}

	public void sendToRegisterNormalQueue(RegisterCouponMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponRegisterNormalRoutingKey, message);
	}
}
