package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.NovaException;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * 쿠폰 메시지를 수신하고 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class CouponReceiver {

	private final CouponSender couponSender;
	private final CouponService couponService;
	private final CouponNotifier couponNotifier;

	/**
	 * rabbitmq 큐로부터 쿠폰 생성 메시지를 수신합니다.
	 *
	 * @param message 유효성 검사를 통과한 CreateCouponMessage 객체
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(@Valid CreateCouponMessage message) {
		RegisterCouponMessage response = couponService.createByMessage(message);
		couponSender.sendToRegisterHighTrafficQueue(response);
	}

	/**
	 * 일반 큐로부터 쿠폰 생성 메시지를 수신합니다.
	 *
	 * @param message 유효성 검사를 통과한 CreateCouponMessage 객체
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(@Valid CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterNormalQueue(response);
		} catch (NovaException e) {
			couponNotifier.notify(message.memberId().toString(), e.getMessage());
		}
	}
}
