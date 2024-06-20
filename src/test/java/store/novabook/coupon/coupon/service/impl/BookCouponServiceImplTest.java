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
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;

class BookCouponServiceImplTest {

	@Mock
	private CouponCodeGenerator codeGenerator;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private BookCouponRepository bookCouponRepository;

	@InjectMocks
	private BookCouponServiceImpl bookCouponService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("책 쿠폰 저장 성공")
	void saveBookCoupon_Success() {
		CreateCouponBookRequest request = new CreateCouponBookRequest(
			1L, "쿠폰 이름", 1000, DiscountType.PERCENT, 5000, 10000, LocalDateTime.now(), LocalDateTime.now().plusDays(10)
		);
		String generatedCode = "B123456789012345";

		when(codeGenerator.generateUniqueCode(CouponType.BOOK)).thenReturn(generatedCode);
		Coupon coupon = Coupon.of(generatedCode, request);
		when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
		BookCoupon bookCoupon = BookCoupon.of(coupon, request.bookId());
		when(bookCouponRepository.save(any(BookCoupon.class))).thenReturn(bookCoupon);

		CreateCouponResponse response = bookCouponService.saveBookCoupon(request);

		assertNotNull(response);
		assertEquals(generatedCode, response.code());
		verify(codeGenerator).generateUniqueCode(CouponType.BOOK);
		verify(couponRepository).save(any(Coupon.class));
		verify(bookCouponRepository).save(any(BookCoupon.class));
	}

	@Test
	@DisplayName("모든 책 쿠폰 가져오기 성공")
	void getCouponBookAll_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		BookCoupon bookCoupon = mock(BookCoupon.class);
		Coupon coupon = new Coupon(
			"B123456789012345",
			"쿠폰 이름",
			1000,
			DiscountType.PERCENT,
			5000,
			10000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10)
		);
		when(bookCoupon.getCoupon()).thenReturn(coupon);
		Page<BookCoupon> bookCouponPage = new PageImpl<>(List.of(bookCoupon));
		when(bookCouponRepository.findAll(pageable)).thenReturn(bookCouponPage);

		Page<GetCouponBookResponse> response = bookCouponService.getCouponBookAll(pageable);

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		verify(bookCouponRepository).findAll(pageable);
	}

	@Test
	@DisplayName("특정 책 쿠폰 가져오기 성공")
	void getCouponBook_Success() {
		Long bookId = 1L;
		BookCoupon bookCoupon = mock(BookCoupon.class);
		Coupon coupon = new Coupon(
			"B123456789012345",
			"쿠폰 이름",
			1000,
			DiscountType.PERCENT,
			5000,
			10000,
			LocalDateTime.now(),
			LocalDateTime.now().plusDays(10)
		);
		when(bookCoupon.getCoupon()).thenReturn(coupon);
		when(bookCoupon.getBookId()).thenReturn(bookId);
		List<BookCoupon> bookCoupons = List.of(bookCoupon);
		when(bookCouponRepository.findAllByBookIdAndCoupon_ExpirationAtAfter(anyLong(), any(LocalDateTime.class)))
			.thenReturn(bookCoupons);

		GetCouponBookAllResponse response = bookCouponService.getCouponBook(bookId);

		assertNotNull(response);
		assertEquals(1, response.couponResponseList().size());
		verify(bookCouponRepository).findAllByBookIdAndCoupon_ExpirationAtAfter(anyLong(), any(LocalDateTime.class));
	}

}
