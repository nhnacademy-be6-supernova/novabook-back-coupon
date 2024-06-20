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
import org.springframework.data.domain.Pageable;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;

class CategoryCouponServiceImplTest {

	@Mock
	private CouponCodeGenerator codeGenerator;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private CategoryCouponRepository categoryCouponRepository;

	@InjectMocks
	private CategoryCouponServiceImpl categoryCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("카테고리 쿠폰 저장 성공")
	void saveCategoryCoupon_Success() {
		CreateCouponCategoryRequest request = new CreateCouponCategoryRequest(
			1L, "쿠폰 이름", 1000, DiscountType.PERCENT, 5000, 10000, LocalDateTime.now(), LocalDateTime.now().plusDays(10)
		);
		String generatedCode = "C123456789012345";

		when(codeGenerator.generateUniqueCode(CouponType.CATEGORY)).thenReturn(generatedCode);
		Coupon coupon = Coupon.of(generatedCode, request);
		when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
		CategoryCoupon categoryCoupon = CategoryCoupon.of(coupon, request.categoryId());
		when(categoryCouponRepository.save(any(CategoryCoupon.class))).thenReturn(categoryCoupon);

		CreateCouponResponse response = categoryCouponService.saveCategoryCoupon(request);

		assertNotNull(response);
		assertEquals(generatedCode, response.code());
		verify(codeGenerator).generateUniqueCode(CouponType.CATEGORY);
		verify(couponRepository).save(any(Coupon.class));
		verify(categoryCouponRepository).save(any(CategoryCoupon.class));
	}

	@Test
	@DisplayName("모든 카테고리 쿠폰 가져오기 성공")
	void getCouponCategoryAll_Success() {
		Pageable pageable = PageRequest.of(0, 10);

		Coupon coupon = new Coupon(
			"C123456789012345",
			"쿠폰 이름",
			1000,
			DiscountType.PERCENT,
			5000,
			10000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10)
		);

		CategoryCoupon categoryCoupon = new CategoryCoupon(coupon, 1L);
		Page<CategoryCoupon> categoryCouponPage = new PageImpl<>(List.of(categoryCoupon));

		when(categoryCouponRepository.findAll(pageable)).thenReturn(categoryCouponPage);

		Page<GetCouponCategoryResponse> response = categoryCouponService.getCouponCategoryAll(pageable);

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		verify(categoryCouponRepository).findAll(pageable);
	}

	@Test
	@DisplayName("카테고리 쿠폰 가져오기 성공")
	void getCouponCategory_Success() {
		Long categoryId = 1L;
		CategoryCoupon categoryCoupon = mock(CategoryCoupon.class);
		when(categoryCoupon.getCoupon()).thenReturn(mock(Coupon.class));
		List<CategoryCoupon> categoryCoupons = List.of(categoryCoupon);
		when(categoryCouponRepository.findAllByCategoryIdAndCoupon_ExpirationAtAfter(anyLong(),
			any(LocalDateTime.class)))
			.thenReturn(categoryCoupons);

		GetCouponCategoryAllResponse response = categoryCouponService.getCouponCategory(categoryId);

		assertNotNull(response);
		assertEquals(1, response.couponResponseList().size());
		verify(categoryCouponRepository).findAllByCategoryIdAndCoupon_ExpirationAtAfter(anyLong(),
			any(LocalDateTime.class));
	}

	@Test
	@DisplayName("카테고리 쿠폰을 찾을 수 없음")
	void getCouponCategory_NotFound() {
		Long categoryId = 1L;
		when(categoryCouponRepository.findAllByCategoryIdAndCoupon_ExpirationAtAfter(anyLong(),
			any(LocalDateTime.class)))
			.thenReturn(List.of());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			categoryCouponService.getCouponCategory(categoryId);
		});

		assertEquals(ErrorCode.CATEGORY_COUPON_NOT_FOUND, exception.getErrorCode());
		verify(categoryCouponRepository).findAllByCategoryIdAndCoupon_ExpirationAtAfter(anyLong(),
			any(LocalDateTime.class));
	}
}
