package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.adapter.dto.RegisterCouponRequest;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.CreateCouponNotifyMessage;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * 쿠폰 메시지를 수신하고 처리하는 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class CouponReceiver {

	public static final String SUCCESS_MESSAGE = "쿠폰이 성공적으로 발급되었습니다.\uD83D\uDCE6";

	private final CouponService couponService;
	private final CouponNotifier couponNotifier;
	private final StoreAdapter storeAdapter;

	/**
	 * rabbitmq 큐로부터 쿠폰 생성 메시지를 수신합니다.
	 *
	 * @param message 유효성 검사를 통과한 CreateCouponMessage 객체
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(@Payload CreateCouponNotifyMessage message,
		@Header("Authorization") String token, @Header("Refresh") String refresh) {
		try {
			CreateCouponResponse response = couponService.createByMessage(CreateCouponMessage.fromEntity(message));
			storeAdapter.registerCoupon(token, refresh,
				RegisterCouponRequest.builder().couponId(response.id()).build());
		} catch (Exception e) {
			couponNotifier.notify(message.uuid(), e.getMessage());
			return;
		}
		couponNotifier.notify(String.valueOf(message.uuid()), SUCCESS_MESSAGE);
	}

	/**
	 * 일반 큐로부터 쿠폰 생성 메시지를 수신합니다.
	 *
	 * @param message 유효성 검사를 통과한 CreateCouponMessage 객체
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(CreateCouponMessage message, @Header("Authorization") String token,
		@Header("Refresh") String refresh) {
		CreateCouponResponse response = couponService.createByMessage(message);
		storeAdapter.registerCoupon(token, refresh, RegisterCouponRequest.builder().couponId(response.id()).build());
	}
}
