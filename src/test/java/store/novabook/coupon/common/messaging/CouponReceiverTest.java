package store.novabook.coupon.common.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.adapter.dto.RegisterCouponRequest;
import store.novabook.coupon.common.dto.ApiResponse;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.common.messaging.dto.CreateCouponNotifyMessage;
import store.novabook.coupon.common.messaging.dto.OrderSagaMessage;
import store.novabook.coupon.common.messaging.dto.PaymentRequest;
import store.novabook.coupon.common.messaging.dto.PaymentType;
import store.novabook.coupon.common.messaging.dto.RegisterCouponMessage;
import store.novabook.coupon.common.messaging.dto.RequestPayCancelMessage;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.service.CouponService;

class CouponReceiverTest {

	@InjectMocks
	private CouponReceiver couponReceiver;

	@Mock
	private CouponService couponService;

	@Mock
	private CouponNotifier couponNotifier;

	@Mock
	private CouponSender couponSender;

	@Mock
	private StoreAdapter storeAdapter;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private HashOperations<String, Object, Object> hashOperations;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testReceiveCreateCouponHighTrafficMessageSuccess() {
		CreateCouponNotifyMessage message = CreateCouponNotifyMessage.builder()
			.uuid("uuid")
			.memberId(1L)
			.couponIdList(Collections.singletonList(1L))
			.couponType(CouponType.BOOK)
			.couponTemplateId(1L)
			.build();

		CreateCouponResponse response = CreateCouponResponse.builder().id(1L).build();

		when(couponService.createByMessage(any(CreateCouponMessage.class))).thenReturn(response);
		when(storeAdapter.registerCoupon(anyString(), anyString(), any(RegisterCouponRequest.class))).thenReturn(
			ApiResponse.success(null));

		couponReceiver.receiveCreateCouponHighTrafficMessage(message, "token", "refresh");

		verify(couponNotifier, times(1)).notify("uuid", CouponReceiver.SUCCESS_MESSAGE);
	}

	@Test
	void testReceiveCreateCouponHighTrafficMessageFailure() {
		CreateCouponNotifyMessage message = CreateCouponNotifyMessage.builder()
			.uuid("uuid")
			.memberId(1L)
			.couponIdList(Collections.singletonList(1L))
			.couponType(CouponType.BOOK)
			.couponTemplateId(1L)
			.build();

		when(couponService.createByMessage(any(CreateCouponMessage.class))).thenThrow(new RuntimeException("Error"));

		couponReceiver.receiveCreateCouponHighTrafficMessage(message, "token", "refresh");

		verify(couponNotifier, times(1)).notify("uuid", "Error");
	}

	@Test
	void testReceiveCreateCouponNormalMessage() {
		CreateCouponMessage message = CreateCouponMessage.builder()
			.memberId(1L)
			.couponIdList(Collections.singletonList(1L))
			.couponType(CouponType.BOOK)
			.couponTemplateId(1L)
			.build();

		CreateCouponResponse response = CreateCouponResponse.builder().id(1L).build();

		when(couponService.createByMessage(any(CreateCouponMessage.class))).thenReturn(response);

		couponReceiver.receiveCreateCouponNormalMessage(message);

		verify(couponSender, times(1)).sendToRegisterNormalQueue(any(RegisterCouponMessage.class));
	}

	@Test
	void testOrderApplyCouponSuccess() {
		PaymentRequest paymentRequest = new PaymentRequest(PaymentType.TOSS, 1L, "orderCode",  null);

		OrderSagaMessage orderSagaMessage = OrderSagaMessage.builder()
			.bookAmount(1000L)
			.calculateTotalAmount(5000L)
			.paymentRequest(paymentRequest)
			.build();

		CouponTemplate couponTemplate = CouponTemplate.builder()
			.discountType(DiscountType.PERCENT)
			.discountAmount(10L)
			.maxDiscountAmount(500L)
			.minPurchaseAmount(1000L)
			.build();

		Coupon coupon = Coupon.builder()
			.couponTemplate(couponTemplate)
			.status(CouponStatus.UNUSED)
			.expirationAt(LocalDateTime.now())
			.build();

		when(redisTemplate.opsForHash()).thenReturn(hashOperations);
		when(hashOperations.get(anyString(), anyString())).thenReturn("1");
		when(couponService.findById(anyLong())).thenReturn(coupon);

		couponReceiver.orderApplyCoupon(orderSagaMessage);

		assertEquals("SUCCESS_APPLY_COUPON", orderSagaMessage.getStatus());
		verify(couponService, times(1)).updateStatus(1L, CouponStatus.UNUSED);
	}

	@Test
	void testCompensateApplyCouponSuccess() {
		PaymentRequest paymentRequest = new PaymentRequest(PaymentType.TOSS, 1L, "orderCode", null);

		OrderSagaMessage orderSagaMessage = OrderSagaMessage.builder().paymentRequest(paymentRequest).build();

		when(redisTemplate.opsForHash()).thenReturn(hashOperations);
		when(hashOperations.get(anyString(), anyString())).thenReturn("1");

		couponReceiver.compensateApplyCoupon(orderSagaMessage);

		verify(couponService, times(1)).updateStatus(1L, CouponStatus.UNUSED);
	}

	@Test
	void testCouponChangeStatusUnUseSuccess() {
		RequestPayCancelMessage message = RequestPayCancelMessage.builder().couponId(1L).build();

		couponReceiver.couponChangeStatusUnUse(message);

		verify(couponService, times(1)).updateStatus(1L, CouponStatus.UNUSED);
	}

}
