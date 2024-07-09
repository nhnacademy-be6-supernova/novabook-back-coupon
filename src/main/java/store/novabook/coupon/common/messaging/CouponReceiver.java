package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.adapter.dto.RegisterCouponRequest;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.CreateCouponNotifyMessage;
import store.novabook.coupon.common.messaging.dto.OrderSagaMessage;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * 쿠폰 메시지를 수신하고 처리하는 서비스 클래스.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponReceiver {

	public static final String SUCCESS_MESSAGE = "쿠폰이 성공적으로 발급되었습니다.\uD83D\uDCE6";

	private final CouponService couponService;
	private final CouponNotifier couponNotifier;
	private final CouponSender couponSender;
	private final StoreAdapter storeAdapter;
	private final RedisTemplate<String, String> redisTemplate;

	// 선착순 쿠폰
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

	// 웰컴쿠폰 ( 토큰이 없음_
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(CreateCouponMessage message) {
		CreateCouponResponse response = couponService.createByMessage(message);
		couponSender.sendToRegisterNormalQueue(
			RegisterCouponMessage.builder().memberId(message.memberId()).couponId(response.id()).build());
	}

	@RabbitListener(queues = "nova.coupon.apply.queue")
	public void orderApplyCoupon(@Payload OrderSagaMessage orderSagaMessage) {
		Long memberId = orderSagaMessage.getPaymentRequest().memberId();
		String key = "OrderForm:" + memberId;
		String field = "couponId";

		HashOperations<String, Object, Object> hashOperation = redisTemplate.opsForHash();
		String stringCouponId = (String) hashOperation.get(key, field);

		if (stringCouponId == null) {
			log.error("Redis에서 쿠폰 ID를 찾을 수 없습니다. 회원 ID: {}", memberId);
			return;
		}

		long couponId;
		try {
			couponId = Long.parseLong(stringCouponId);
		} catch (NumberFormatException e) {
			log.error("잘못된 쿠폰 ID 형식입니다: {}", stringCouponId, e);
			return;
		}

		Coupon coupon;
		try {
			coupon = couponService.findById(couponId);
		} catch (Exception e) {
			log.error("해당 쿠폰 ID로 쿠폰을 찾을 수 없습니다: {}", couponId, e);
			return;
		}

		CouponTemplate couponTemplate = coupon.getCouponTemplate();

		if (couponTemplate.getDiscountType() == DiscountType.PERCENT) {
			long totalAmount = orderSagaMessage.getCalculateTotalAmount();
			long minPurchaseAmount = couponTemplate.getMinPurchaseAmount();

			if (totalAmount < minPurchaseAmount) {
				throw new IllegalArgumentException("구매금액이 쿠폰 최저 구매가보다 낮습니다.");
			}

			long applyAmount = totalAmount * couponTemplate.getDiscountAmount() / 100;

			if (applyAmount > couponTemplate.getMaxDiscountAmount()) {
				log.info("쿠폰 할인 금액이 최대 할인 금액보다 높아 최대할인가로 적용합니다.");
				applyAmount = couponTemplate.getMaxDiscountAmount();
			}

			orderSagaMessage.setCalculateTotalAmount(totalAmount - applyAmount);
			log.debug("쿠폰 적용가 {}", applyAmount);
		}

		couponService.updateStatusToUsed(couponId);
		orderSagaMessage.setStatus("SUCCESS_APPLY_COUPON");
		couponSender.sendToApplyCouponQueue(orderSagaMessage);
	}
}
