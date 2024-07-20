package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.repository.LimitedCouponTemplateRepository;

class CouponServiceImplTest {

	@InjectMocks
	private CouponServiceImpl couponService;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private CouponTemplateRepository couponTemplateRepository;

	@Mock
	private LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("쿠폰 상태 업데이트 - 사용")
	void updateStatusToUsed() {
		Coupon coupon = createMockCoupon();
		when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));

		couponService.updateStatusToUsed(coupon.getId());

		assertEquals(CouponStatus.USED, coupon.getStatus());
		verify(couponRepository, times(1)).findById(coupon.getId());
	}

	@Test
	@DisplayName("이미 사용된 쿠폰 상태 업데이트")
	void updateStatusToUsed_AlreadyUsed() {
		Coupon coupon = createMockCoupon();
		coupon.updateStatus(CouponStatus.USED);
		when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> couponService.updateStatusToUsed(coupon.getId()));

		assertEquals(ErrorCode.ALREADY_USED_COUPON, exception.getErrorCode());
	}

	@Test
	@DisplayName("쿠폰 상태 업데이트")
	void updateStatus() {
		Coupon coupon = createMockCoupon();
		when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));

		couponService.updateStatus(coupon.getId(), CouponStatus.USED);

		assertEquals(CouponStatus.USED, coupon.getStatus());
		verify(couponRepository, times(1)).findById(coupon.getId());
	}

	@Test
	@DisplayName("유효한 모든 쿠폰 조회")
	void findAllValidById() {
		List<Long> couponIdList = List.of(1L);
		Coupon coupon = createMockCoupon();
		when(couponRepository.findAllByIdInAndStatusAndExpirationAtAfter(any(), any(), any())).thenReturn(
			List.of(coupon));

		GetCouponAllResponse response = couponService.findAllValidById(couponIdList);

		assertNotNull(response);
		assertEquals(1, response.couponResponseList().size());

		ArgumentCaptor<List> couponIdCaptor = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<CouponStatus> statusCaptor = ArgumentCaptor.forClass(CouponStatus.class);
		ArgumentCaptor<LocalDateTime> timeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		verify(couponRepository, times(1)).findAllByIdInAndStatusAndExpirationAtAfter(couponIdCaptor.capture(),
			statusCaptor.capture(), timeCaptor.capture());

		assertEquals(couponIdList, couponIdCaptor.getValue());
		assertEquals(CouponStatus.UNUSED, statusCaptor.getValue());
		assertTrue(timeCaptor.getValue().isBefore(LocalDateTime.now().plusSeconds(1)) && timeCaptor.getValue()
			.isAfter(LocalDateTime.now().minusSeconds(1)));
	}

	@Test
	@DisplayName("메시지를 통한 쿠폰 생성")
	void createByMessage() {
		CreateCouponMessage message = createMockCreateCouponMessage();
		CouponTemplate couponTemplate = createMockCouponTemplate();
		when(couponTemplateRepository.findById(message.couponTemplateId())).thenReturn(Optional.of(couponTemplate));
		Coupon savedCoupon = createMockCoupon();
		when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

		CreateCouponResponse response = couponService.createByMessage(message);

		assertNotNull(response);
		verify(couponTemplateRepository, times(1)).findById(message.couponTemplateId());
		verify(couponRepository, times(1)).save(any(Coupon.class));
	}

	private Coupon createMockCoupon() {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Sample Coupon")
			.type(CouponType.GENERAL)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();

		Coupon coupon = spy(Coupon.builder()
			.couponTemplate(couponTemplate)
			.status(CouponStatus.UNUSED)
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build());

		doReturn(1L).when(coupon).getId();

		return coupon;
	}

	private CouponTemplate createMockCouponTemplate() {
		return spy(createMockCouponTemplateBuilder().build());
	}

	private CouponTemplate.CouponTemplateBuilder createMockCouponTemplateBuilder() {
		return CouponTemplate.builder()
			.name("Sample Coupon")
			.type(CouponType.GENERAL)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30);
	}

	private CreateCouponMessage createMockCreateCouponMessage() {
		return CreateCouponMessage.builder()
			.memberId(1L)
			.couponIdList(List.of(1L))
			.couponType(CouponType.GENERAL)
			.couponTemplateId(1L)
			.build();
	}
}
