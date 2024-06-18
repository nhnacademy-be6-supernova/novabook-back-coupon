package store.novabook.coupon.coupon.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.UpdateCouponExpirationRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;

@SpringBootTest
@Sql(scripts = {"/test-coupon.sql"})
public class CouponServiceImplTest {

	@Autowired
	private CouponServiceImpl couponService;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private BookCouponRepository bookCouponRepository;

	@Autowired
	private CategoryCouponRepository categoryCouponRepository;

	@Test
	@DisplayName("일반 쿠폰 저장 성공")
	public void saveGeneralCoupon_Success() {
		CreateCouponRequest request = new CreateCouponRequest("일반 쿠폰", 1000, DiscountType.AMOUNT, 5000, 10000,
			LocalDateTime.now(), LocalDateTime.now().plusDays(10));
		CreateCouponResponse response = couponService.saveGeneralCoupon(request);

		Optional<Coupon> savedCoupon = couponRepository.findById(response.code());
		assertThat(savedCoupon).isPresent();
		assertThat(savedCoupon.get().getName()).isEqualTo("일반 쿠폰");
	}

	@Test
	@DisplayName("책 쿠폰 저장 성공")
	public void saveBookCoupon_Success() {
		CreateBookCouponRequest request = new CreateBookCouponRequest(1L, "책 쿠폰", 1500, DiscountType.AMOUNT, 7000, 5000,
			LocalDateTime.now(), LocalDateTime.now().plusDays(20));
		CreateCouponResponse response = couponService.saveBookCoupon(request);

		Optional<Coupon> savedCoupon = couponRepository.findById(response.code());
		Optional<BookCoupon> savedBookCoupon = bookCouponRepository.findById(response.code());
		assertThat(savedCoupon).isPresent();
		assertThat(savedBookCoupon).isPresent();
		assertThat(savedCoupon.get().getName()).isEqualTo("책 쿠폰");
		assertThat(savedBookCoupon.get().getBookId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("카테고리 쿠폰 저장 성공")
	public void saveCategoryCoupon_Success() {
		CreateCategoryCouponRequest request = new CreateCategoryCouponRequest(2L, "카테고리 쿠폰", 2000, DiscountType.PERCENT,
			8000, 4000, LocalDateTime.now(), LocalDateTime.now().plusDays(15));
		CreateCouponResponse response = couponService.saveCategoryCoupon(request);

		Optional<Coupon> savedCoupon = couponRepository.findById(response.code());
		Optional<CategoryCoupon> savedCategoryCoupon = categoryCouponRepository.findById(response.code());
		assertThat(savedCoupon).isPresent();
		assertThat(savedCategoryCoupon).isPresent();
		assertThat(savedCoupon.get().getName()).isEqualTo("카테고리 쿠폰");
		assertThat(savedCategoryCoupon.get().getCategoryId()).isEqualTo(2L);
	}

	@Test
	@DisplayName("쿠폰 만료 시간 업데이트 성공")
	@Sql("/test-coupon.sql")
	public void updateCouponExpiration_Success() {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("C123456789012345");
		couponService.updateCouponExpiration(request);

		Optional<Coupon> updatedCoupon = couponRepository.findById("C123456789012345");
		assertThat(updatedCoupon).isPresent();
		assertThat(updatedCoupon.get().getExpirationAt()).isBefore(LocalDateTime.now().plusSeconds(1));
	}

	@Test
	@DisplayName("쿠폰 만료 시간 업데이트 실패 - 쿠폰 찾을 수 없음")
	public void updateCouponExpiration_Fail_CouponNotFound() {
		UpdateCouponExpirationRequest request = new UpdateCouponExpirationRequest("NON_EXISTENT_CODE");

		assertThatThrownBy(() -> couponService.updateCouponExpiration(request))
			.isInstanceOf(NotFoundException.class)
			.hasMessageContaining(ErrorCode.COUPON_NOT_FOUND.getMessage());
	}
}
