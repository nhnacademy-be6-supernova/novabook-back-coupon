package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponReceiver {

	private final CouponSender couponSender;
	private final CouponService couponService;

	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterHighTrafficQueue(response);        // 쿠폰 생성 후 회원 서버로 쿠폰 등록 메시지 전송

		} catch (NotFoundException ignored) {
			// ignore
		}
	}
}
