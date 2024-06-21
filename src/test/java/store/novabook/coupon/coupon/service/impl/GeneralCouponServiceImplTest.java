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

import store.novabook.coupon.common.util.CouponCodeGenerator;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.repository.CouponRepository;

class GeneralCouponServiceImplTest {

	@Mock
	private CouponCodeGenerator codeGenerator;

	@Mock
	private CouponRepository couponRepository;

	@InjectMocks
	private GeneralCouponServiceImpl generalCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("일반 쿠폰 저장 성공")
	void saveGeneralCoupon_Success() {
		CreateCouponRequest request = new CreateCouponRequest("쿠폰 이름", 1000, DiscountType.PERCENT, 5000, 10000,
			LocalDateTime.now(), LocalDateTime.now().plusDays(10));
		String generatedCode = "G123456789012345";

		when(codeGenerator.generateUniqueCode(CouponType.GENERAL)).thenReturn(generatedCode);
		Coupon coupon = Coupon.of(generatedCode, request);
		when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

		CreateCouponResponse response = generalCouponService.saveGeneralCoupon(request);

		assertNotNull(response);
		assertEquals(generatedCode, response.code());
		verify(codeGenerator).generateUniqueCode(CouponType.GENERAL);
		verify(couponRepository).save(any(Coupon.class));
	}

	@Test
	@DisplayName("모든 일반 쿠폰 가져오기 성공")
	void getCouponGeneralAll_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		Coupon coupon = mock(Coupon.class);
		Page<Coupon> couponPage = new PageImpl<>(List.of(coupon));
		when(couponRepository.findAllByCodeStartsWith(CouponType.GENERAL.getPrefix(), pageable)).thenReturn(couponPage);

		Page<GetCouponResponse> response = generalCouponService.getCouponGeneralAll(pageable);

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		verify(couponRepository).findAllByCodeStartsWith(CouponType.GENERAL.getPrefix(), pageable);
	}
}
