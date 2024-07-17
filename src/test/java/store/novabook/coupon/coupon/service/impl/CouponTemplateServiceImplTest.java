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

import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;

class CouponTemplateServiceImplTest {

	@Mock
	private CouponTemplateRepository couponTemplateRepository;

	@InjectMocks
	private CouponTemplateServiceImpl couponTemplateService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindByTypeAndValid_Valid() {
		CouponType type = CouponType.GENERAL;
		Pageable pageable = PageRequest.of(0, 10);

		List<CouponTemplate> templateList = List.of(createMockCouponTemplate(type),
			createMockCouponTemplate(type));
		Page<CouponTemplate> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

		when(couponTemplateRepository.findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(eq(type),
			any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable))).thenReturn(templatePage);

		Page<GetCouponTemplateResponse> result = couponTemplateService.findByTypeAndValid(type, true, pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(couponTemplateRepository, times(1)).findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(eq(type),
			any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable));
	}

	@Test
	void testFindByTypeAndValid_NotValid() {
		CouponType type = CouponType.GENERAL;
		Pageable pageable = PageRequest.of(0, 10);

		List<CouponTemplate> templateList = List.of(createMockCouponTemplate(type),
			createMockCouponTemplate(type));
		Page<CouponTemplate> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

		when(couponTemplateRepository.findAllByType(type, pageable)).thenReturn(templatePage);

		Page<GetCouponTemplateResponse> result = couponTemplateService.findByTypeAndValid(type, false, pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(couponTemplateRepository, times(1)).findAllByType(type, pageable);
	}

	@Test
	void testFindAll() {
		Pageable pageable = PageRequest.of(0, 10);
		List<CouponTemplate> templateList = List.of(createMockCouponTemplate(CouponType.GENERAL),
			createMockCouponTemplate(CouponType.BIRTHDAY));
		Page<CouponTemplate> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

		when(couponTemplateRepository.findAll(pageable)).thenReturn(templatePage);

		Page<GetCouponTemplateResponse> result = couponTemplateService.findAll(pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		verify(couponTemplateRepository, times(1)).findAll(pageable);
	}

	@Test
	void testCreate_InvalidType() {
		CreateCouponTemplateRequest request = new CreateCouponTemplateRequest("Test Coupon", CouponType.BOOK, 1000L,
			DiscountType.AMOUNT, 5000L, 10000L, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);

		assertThrows(BadRequestException.class, () -> couponTemplateService.create(request));
		verify(couponTemplateRepository, never()).save(any(CouponTemplate.class));
	}

	private CouponTemplate createMockCouponTemplate(CouponType type) {
		return CouponTemplate.builder()
			.name("Test Coupon")
			.type(type)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now())
			.expirationAt(LocalDateTime.now().plusDays(30))
			.usePeriod(7)
			.build();
	}
}