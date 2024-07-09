package store.novabook.coupon.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.adapter.dto.RegisterCouponRequest;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
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

	/**
	 * 주문서에서 couponID를 가져와 검증합니다.
	 * coupon을 적용합니다.
	 * @param orderSagaMessage
	 */
	@RabbitListener(queues = "nova.coupon.apply.queue")
	@Transactional
	public void orderApplyCoupon(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();
			String key = "OrderForm:" + memberId;
			String field = "couponId";

			HashOperations<String, Object, Object> hashOperation = redisTemplate.opsForHash();
			String stringCouponId = (String) hashOperation.get(key, field);

			if (stringCouponId == null) {
				throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
			}

			long couponId = Long.parseLong(stringCouponId);
			Coupon coupon = couponService.findById(couponId);
			CouponTemplate couponTemplate = coupon.getCouponTemplate();

			applyCouponDiscount(orderSagaMessage, couponTemplate);

			couponService.updateStatusToUsed(couponId);
			orderSagaMessage.setStatus("SUCCESS_APPLY_COUPON");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_APPLY_COUPON");
			throw e;
		} finally {
			couponSender.sendToApplyCouponQueue(orderSagaMessage);
		}
	}

	private void applyCouponDiscount(OrderSagaMessage orderSagaMessage, CouponTemplate couponTemplate) {
		if (couponTemplate.getDiscountType() == DiscountType.PERCENT) {
			long totalAmount = orderSagaMessage.getCalculateTotalAmount();
			long minPurchaseAmount = couponTemplate.getMinPurchaseAmount();

			if (totalAmount < minPurchaseAmount) {
				throw new IllegalArgumentException("구매금액이 쿠폰 최저 구매가보다 낮습니다.");
			}

			long applyAmount = calculateDiscountAmount(totalAmount, couponTemplate);
			orderSagaMessage.setCalculateTotalAmount(totalAmount - applyAmount);

			log.debug("쿠폰 적용가 {}", applyAmount);

		} else if(couponTemplate.getDiscountType() == DiscountType.AMOUNT) {
			long totalAmount = orderSagaMessage.getCalculateTotalAmount();
			orderSagaMessage.setCalculateTotalAmount(totalAmount - couponTemplate.getDiscountAmount());

			log.debug("쿠폰 적용가 {}", couponTemplate.getDiscountAmount());
		}
	}

	private long calculateDiscountAmount(long totalAmount, CouponTemplate couponTemplate) {
		long applyAmount = totalAmount * couponTemplate.getDiscountAmount() / 100;

		if (applyAmount > couponTemplate.getMaxDiscountAmount()) {
			log.info("쿠폰 할인 금액이 최대 할인 금액보다 높아 최대할인가로 적용합니다.");
			applyAmount = couponTemplate.getMaxDiscountAmount();
		}

		return applyAmount;
	}

}
