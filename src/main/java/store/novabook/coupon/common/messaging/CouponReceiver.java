package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.NovaException;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.common.security.aop.CurrentMembers;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
public class CouponReceiver {

	private final CouponSender couponSender;
	private final CouponService couponService;
	private final CouponNotifier couponNotifier;

	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(@CurrentMembers Long clientId,
		@Valid CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterHighTrafficQueue(response);        // 쿠폰 생성 후 회원 서버로 쿠폰 등록 메시지 전송
		} catch (NovaException e) {
			couponNotifier.notify(clientId.toString(), e.getMessage());
		}
	}

	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(@CurrentMembers Long clientId, @Valid CreateCouponMessage message) {
		try {
			RegisterCouponMessage response = couponService.createByMessage(message);
			couponSender.sendToRegisterNormalQueue(response);        // 쿠폰 생성 후 회원 서버로 쿠폰 등록 메시지 전송
		} catch (NovaException e) {
			couponNotifier.notify(String.valueOf(clientId), e.getMessage());
		}
	}

	@ExceptionHandler(Exception.class)
	public void handleException(Exception ex, @CurrentMembers Long clientId) {
		couponNotifier.notify(String.valueOf(clientId), "쿠폰 발급이 실패하였습니다.");
	}
}
