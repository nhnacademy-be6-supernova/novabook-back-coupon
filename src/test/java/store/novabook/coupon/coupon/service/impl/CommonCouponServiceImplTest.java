package store.novabook.coupon.coupon.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.repository.CouponRepository;

class CommonCouponServiceImplTest {

	@Mock
	private CouponRepository couponRepository;

	@InjectMocks
	private CommonCouponServiceImpl commonCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("쿠폰 강제종료 성공")
	void updateCouponExpiration_Success() {
		String couponCode = "G123456789012345";
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest(couponCode);

		Coupon coupon = mock(Coupon.class);
		when(couponRepository.findById(couponCode)).thenReturn(Optional.of(coupon));

		commonCouponService.updateCouponExpiration(request);

		verify(coupon).updateExprationAt(any(LocalDateTime.class));
		verify(couponRepository).save(coupon);
	}

	@Test
	@DisplayName("쿠폰을 찾을 수 없음")
	void updateCouponExpiration_NotFound() {
		String couponCode = "G123456789012345";
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest(couponCode);

		when(couponRepository.findById(couponCode)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			commonCouponService.updateCouponExpiration(request);
		});

		assertEquals(ErrorCode.COUPON_NOT_FOUND, exception.getErrorCode());
		verify(couponRepository).findById(couponCode);
	}
}
