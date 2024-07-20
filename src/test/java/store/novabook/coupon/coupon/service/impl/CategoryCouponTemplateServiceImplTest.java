package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCategoryCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.repository.CategoryCouponTemplateRepository;

class CategoryCouponTemplateServiceImplTest {

	@InjectMocks
	private CategoryCouponTemplateServiceImpl categoryCouponTemplateService;

	@Mock
	private CategoryCouponTemplateRepository categoryCouponTemplateRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("모든 카테고리 쿠폰 템플릿 페이지 형식으로 조회")
	void findAll() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		CategoryCouponTemplate categoryCouponTemplate = createMockCategoryCouponTemplate();
		Page<CategoryCouponTemplate> page = new PageImpl<>(List.of(categoryCouponTemplate));

		when(categoryCouponTemplateRepository.findAll(pageRequest)).thenReturn(page);

		Page<GetCategoryCouponTemplateResponse> responsePage = categoryCouponTemplateService.findAll(pageRequest);

		assertNotNull(responsePage);
		assertEquals(1, responsePage.getTotalElements());
		verify(categoryCouponTemplateRepository, times(1)).findAll(pageRequest);
	}

	@Test
	@DisplayName("유효한 모든 카테고리 쿠폰 템플릿 페이지 형식으로 조회")
	void findAllWithValid() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		CategoryCouponTemplate categoryCouponTemplate = createMockCategoryCouponTemplate();
		Page<CategoryCouponTemplate> page = new PageImpl<>(List.of(categoryCouponTemplate));

		when(categoryCouponTemplateRepository.findAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter(
			any(LocalDateTime.class), any(LocalDateTime.class), eq(pageRequest))).thenReturn(page);

		Page<GetCategoryCouponTemplateResponse> responsePage = categoryCouponTemplateService.findAllWithValid(
			pageRequest);

		assertNotNull(responsePage);
		assertEquals(1, responsePage.getTotalElements());
		verify(categoryCouponTemplateRepository, times(1))
			.findAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter(
				any(LocalDateTime.class), any(LocalDateTime.class), eq(pageRequest));
	}

	@Test
	@DisplayName("새로운 카테고리 쿠폰 템플릿 생성")
	void create() {
		CreateCategoryCouponTemplateRequest request = new CreateCategoryCouponTemplateRequest(
			1L, "Sample Category Coupon", 1000L, DiscountType.AMOUNT, 5000L, 10000L,
			LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(10), 30);
		CategoryCouponTemplate categoryCouponTemplate = CategoryCouponTemplate.of(request);
		CategoryCouponTemplate saved = createMockCategoryCouponTemplate();

		when(categoryCouponTemplateRepository.save(any(CategoryCouponTemplate.class))).thenReturn(saved);

		CreateCouponTemplateResponse response = categoryCouponTemplateService.create(request);

		assertNotNull(response);
		assertEquals(saved.getId(), response.id());
		verify(categoryCouponTemplateRepository, times(1)).save(any(CategoryCouponTemplate.class));
	}

	@Test
	@DisplayName("유효한 카테고리 ID로 카테고리 쿠폰 템플릿 조회")
	void findAllByCategoryId_Valid() {
		List<Long> categoryIdList = List.of(1L);
		CategoryCouponTemplate categoryCouponTemplate = createMockCategoryCouponTemplate();
		when(
			categoryCouponTemplateRepository.findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				anyList(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(
			List.of(categoryCouponTemplate));

		GetCategoryCouponTemplateAllResponse response = categoryCouponTemplateService.findAllByCategoryId(
			categoryIdList, true);

		assertNotNull(response);
		assertEquals(1, response.responseList().size());
		verify(categoryCouponTemplateRepository, times(1))
			.findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				anyList(), any(LocalDateTime.class), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("모든 카테고리 ID로 카테고리 쿠폰 템플릿 조회")
	void findAllByCategoryId_All() {
		List<Long> categoryIdList = List.of(1L);
		CategoryCouponTemplate categoryCouponTemplate = createMockCategoryCouponTemplate();
		when(categoryCouponTemplateRepository.findAllByCategoryIdIn(anyList())).thenReturn(
			List.of(categoryCouponTemplate));

		GetCategoryCouponTemplateAllResponse response = categoryCouponTemplateService.findAllByCategoryId(
			categoryIdList, false);

		assertNotNull(response);
		assertEquals(1, response.responseList().size());
		verify(categoryCouponTemplateRepository, times(1)).findAllByCategoryIdIn(anyList());
	}

	private CategoryCouponTemplate createMockCategoryCouponTemplate() {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Sample Coupon")
			.type(CouponType.CATEGORY)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();

		return CategoryCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.categoryId(1L)
			.build();
	}
}
