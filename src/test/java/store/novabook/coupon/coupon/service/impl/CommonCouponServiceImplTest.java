package store.novabook.coupon.coupon.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.repository.CouponRepository;

class CommonCouponServiceImplTest {

	@InjectMocks
	private CommonCouponServiceImpl commonCouponService;

	@Mock
	private CouponRepository couponRepository;

	private Coupon validCoupon;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		validCoupon = Coupon.builder()
			.code("VALIDCODE")
			.name("Valid Coupon")
			.discountAmount(1000)
			.discountType(DiscountType.PERCENT)
			.maxDiscountAmount(5000)
			.minPurchaseAmount(100)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(7))
			.build();
	}

	@Test
	@DisplayName("쿠폰 만료일 업데이트 테스트 - 성공")
	void updateCouponExpirationTestSuccess() {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("VALIDCODE");

		given(couponRepository.findById("VALIDCODE")).willReturn(Optional.of(validCoupon));

		commonCouponService.updateCouponExpiration(request);

		assertThat(validCoupon.getExpirationAt()).isBefore(LocalDateTime.now());
	}

	@Test
	@DisplayName("쿠폰 만료일 업데이트 테스트 - 실패 (쿠폰 없음)")
	void updateCouponExpirationTestFailureCouponNotFound() {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("INVALIDCODE");

		given(couponRepository.findById("INVALIDCODE")).willReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			commonCouponService.updateCouponExpiration(request);
		});
	}
}
