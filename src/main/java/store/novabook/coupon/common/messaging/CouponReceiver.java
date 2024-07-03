package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.NovaException;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponReceiver {

	public static final Long CLIENT_ID = 3L;
	private final CouponSender couponSender;
	private final CouponService couponService;
	private final CouponNotifier couponNotifier;

	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(@Valid CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterHighTrafficQueue(response);        // 쿠폰 생성 후 회원 서버로 쿠폰 등록 메시지 전송
		} catch (NovaException e) {
			couponNotifier.notify(CLIENT_ID.toString(), e.getMessage());
		}
	}

	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(@Valid CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterNormalQueue(response);        // 쿠폰 생성 후 회원 서버로 쿠폰 등록 메시지 전송
		} catch (NovaException e) {
			couponNotifier.notify(CLIENT_ID.toString(), e.getMessage());
		}
	}

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex) {
		couponNotifier.notify(CLIENT_ID.toString(), "쿠폰 발급이 실패하였습니다.");
	}
}
