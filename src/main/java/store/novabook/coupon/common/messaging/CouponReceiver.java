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
import store.novabook.coupon.common.messaging.dto.RequestPayCancelMessage;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * 쿠폰 메시지를 수신하고 처리하는 서비스 클래스입니다.
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

	/**
	 * 선착순 쿠폰 생성 메시지를 수신하여 처리합니다.
	 *
	 * @param message 쿠폰 생성 알림 메시지
	 * @param token   인증 토큰
	 * @param refresh 갱신 토큰
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateHighTraffic}")
	public void receiveCreateCouponHighTrafficMessage(@Payload CreateCouponNotifyMessage message,
		@Header("Authorization") String token,
		@Header("Refresh") String refresh) {
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
	 * 웰컴 쿠폰 생성 메시지를 수신하여 처리합니다.
	 *
	 * @param message 쿠폰 생성 메시지
	 */
	@RabbitListener(queues = "${rabbitmq.queue.couponCreateNormal}")
	public void receiveCreateCouponNormalMessage(CreateCouponMessage message) {
		CreateCouponResponse response = couponService.createByMessage(message);
		couponSender.sendToRegisterNormalQueue(
			RegisterCouponMessage.builder().memberId(message.memberId()).couponId(response.id()).build());
	}

	/**
	 * 주문서에서 쿠폰 ID를 가져와 검증하고 쿠폰을 적용합니다.
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.coupon.apply.queue")
	@Transactional
	public void orderApplyCoupon(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();
			String key = "OrderForm:" + memberId;
			String field = "couponId";

			HashOperations<String, Object, Object> hashOperation = redisTemplate.opsForHash();
			String stringCouponId = (String)hashOperation.get(key, field);

			if (stringCouponId == null) {
				throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
			}

			long couponId = Long.parseLong(stringCouponId);
			Coupon coupon = couponService.findById(couponId);
			CouponTemplate couponTemplate = coupon.getCouponTemplate();

			applyCouponDiscount(orderSagaMessage, couponTemplate);

			couponService.updateStatus(couponId, CouponStatus.UNUSED);
			orderSagaMessage.setStatus("SUCCESS_APPLY_COUPON");
		} catch (Exception e) {
			log.error("", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_APPLY_COUPON");
		} finally {
			couponSender.sendToApplyCouponQueue(orderSagaMessage);
		}
	}

	private void applyCouponDiscount(OrderSagaMessage orderSagaMessage, CouponTemplate couponTemplate) {
		if (couponTemplate.getDiscountType() == DiscountType.PERCENT) {

			long bookAmount = orderSagaMessage.getBookAmount();
			long minPurchaseAmount = couponTemplate.getMinPurchaseAmount();

			if (bookAmount < minPurchaseAmount) {
				throw new IllegalArgumentException("도서 순수 금액이 쿠폰 최저 구매가보다 낮습니다.");
			}

			long applyAmount = calculateDiscountAmount(bookAmount, couponTemplate);
			orderSagaMessage.setCalculateTotalAmount(orderSagaMessage.getCalculateTotalAmount() - applyAmount);
			orderSagaMessage.setCouponAmount(applyAmount);

			log.debug("쿠폰 적용가 {}", applyAmount);
			log.debug("쿠폰 적용 후  {}", couponTemplate.getDiscountAmount());

		} else if (couponTemplate.getDiscountType() == DiscountType.AMOUNT) {
			long totalAmount = orderSagaMessage.getCalculateTotalAmount();
			orderSagaMessage.setCalculateTotalAmount(totalAmount - couponTemplate.getDiscountAmount());

			log.debug("쿠폰 적용가 {}", couponTemplate.getDiscountAmount());
		}
	}

	private long calculateDiscountAmount(long bookAmount, CouponTemplate couponTemplate) {
		float applyAmount = bookAmount * ((float)couponTemplate.getDiscountAmount() / 100);

		if (applyAmount > couponTemplate.getMaxDiscountAmount()) {
			log.info("쿠폰 할인 금액이 최대 할인 금액보다 높아 최대할인가로 적용합니다.");
			applyAmount = couponTemplate.getMaxDiscountAmount();
		}

		return (long)applyAmount;
	}

	/**
	 * 보상 트랜잭션 로직을 처리합니다.
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.coupon.compensate.apply.queue")
	@Transactional
	public void compensateApplyCoupon(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();
			String key = "OrderForm:" + memberId;
			String field = "couponId";

			HashOperations<String, Object, Object> hashOperation = redisTemplate.opsForHash();
			String stringCouponId = (String)hashOperation.get(key, field);

			if (stringCouponId == null) {
				throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
			}

			long couponId = Long.parseLong(stringCouponId);
			couponService.updateStatus(couponId, CouponStatus.UNUSED);
			log.info("success coupon compensate transaction");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_COMPENSATE_APPLY_COUPON");
			couponSender.sendToCompensateApplyCouponQueue(orderSagaMessage);
		}
	}

	/**
	 * 결제 취소 요청 메시지를 수신하여 쿠폰 상태를 미사용으로 변경합니다.
	 *
	 * @param message 결제 취소 요청 메시지
	 */
	@RabbitListener(queues = "nova.coupon.request.pay.cancel.queue")
	public void couponChangeStatusUnUse(@Payload RequestPayCancelMessage message) {
		try {
			if (message.getCouponId() == null) {
				throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
			}
			couponService.updateStatus(message.getCouponId(), CouponStatus.UNUSED);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			message.setStatus("FAIL_COUPON_CHANGE_UNUSE_STATUS");
			couponSender.sendToRequestPayCancelQueue(message);
		}
	}

}
