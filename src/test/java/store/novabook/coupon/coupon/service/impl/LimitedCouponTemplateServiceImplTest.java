package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetLimitedCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.LimitedCouponTemplate;
import store.novabook.coupon.coupon.repository.LimitedCouponTemplateRepository;

class LimitedCouponTemplateServiceImplTest {

	@Mock
	private LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	@InjectMocks
	private LimitedCouponTemplateServiceImpl limitedCouponTemplateService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreate() {
		CreateLimitedCouponTemplateRequest request = new CreateLimitedCouponTemplateRequest(
			100L, "Test Coupon", 1000L, DiscountType.AMOUNT, 5000L, 10000L,
			LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7
		);

		LimitedCouponTemplate savedTemplate = new LimitedCouponTemplate(
			CouponTemplate.of(request),
			request.quantity()
		);
		when(limitedCouponTemplateRepository.save(any(LimitedCouponTemplate.class))).thenReturn(savedTemplate);

		CreateCouponTemplateResponse response = limitedCouponTemplateService.create(request);

		assertNotNull(response);
		verify(limitedCouponTemplateRepository, times(1)).save(any(LimitedCouponTemplate.class));
	}

	@Test
	void testFindAll() {
		Pageable pageable = PageRequest.of(0, 10);
		List<LimitedCouponTemplate> templateList = List.of(
			createMockLimitedCouponTemplate(100L),
			createMockLimitedCouponTemplate(200L)
		);
		Page<LimitedCouponTemplate> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

		when(limitedCouponTemplateRepository.findAll(pageable)).thenReturn(templatePage);

		Page<GetLimitedCouponTemplateResponse> result = limitedCouponTemplateService.findAll(pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(limitedCouponTemplateRepository, times(1)).findAll(pageable);
	}

	@Test
	void testFindAllWithValid() {
		Pageable pageable = PageRequest.of(0, 10);
		List<LimitedCouponTemplate> templateList = List.of(
			createMockLimitedCouponTemplate(100L),
			createMockLimitedCouponTemplate(200L)
		);
		Page<LimitedCouponTemplate> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

		when(
			limitedCouponTemplateRepository.findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
				any(LocalDateTime.class), any(LocalDateTime.class), eq(LimitedCouponTemplateServiceImpl.MIN_QUANTITY),
				eq(pageable)
			)).thenReturn(templatePage);

		Page<GetLimitedCouponTemplateResponse> result = limitedCouponTemplateService.findAllWithValid(pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(limitedCouponTemplateRepository,
			times(1)).findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
			any(LocalDateTime.class), any(LocalDateTime.class), eq(LimitedCouponTemplateServiceImpl.MIN_QUANTITY),
			eq(pageable)
		);
	}

	private LimitedCouponTemplate createMockLimitedCouponTemplate(Long quantity) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon")
			.type(CouponType.LIMITED)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(30))
			.usePeriod(7)
			.build();

		return new LimitedCouponTemplate(couponTemplate, quantity);
	}
}