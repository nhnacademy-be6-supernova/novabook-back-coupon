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

import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateAllResponse;
import store.novabook.coupon.coupon.dto.response.GetBookCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.repository.BookCouponTemplateRepository;

class BookCouponTemplateServiceImplTest {

	@InjectMocks
	private BookCouponTemplateServiceImpl bookCouponTemplateService;

	@Mock
	private BookCouponTemplateRepository bookCouponTemplateRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("모든 도서 쿠폰 템플릿 페이지 형식으로 조회")
	void findAll() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		BookCouponTemplate bookCouponTemplate = createMockBookCouponTemplate();
		Page<BookCouponTemplate> page = new PageImpl<>(List.of(bookCouponTemplate));

		when(bookCouponTemplateRepository.findAll(pageRequest)).thenReturn(page);

		Page<GetBookCouponTemplateResponse> responsePage = bookCouponTemplateService.findAll(pageRequest);

		assertNotNull(responsePage);
		assertEquals(1, responsePage.getTotalElements());
		verify(bookCouponTemplateRepository, times(1)).findAll(pageRequest);
	}

	@Test
	@DisplayName("새로운 도서 쿠폰 템플릿 생성")
	void create() {
		CreateBookCouponTemplateRequest request = new CreateBookCouponTemplateRequest(
			1L, "Sample Book Coupon", 1000L, DiscountType.AMOUNT, 5000L, 10000L,
			LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(10), 30);
		BookCouponTemplate bookCouponTemplate = BookCouponTemplate.of(request);
		BookCouponTemplate saved = createMockBookCouponTemplate();

		when(bookCouponTemplateRepository.save(any(BookCouponTemplate.class))).thenReturn(saved);

		CreateCouponTemplateResponse response = bookCouponTemplateService.create(request);

		assertNotNull(response);
		assertEquals(saved.getId(), response.id());
		verify(bookCouponTemplateRepository, times(1)).save(any(BookCouponTemplate.class));
	}

	@Test
	@DisplayName("유효한 도서 ID로 도서 쿠폰 템플릿 조회")
	void findAllByBookId_Valid() {
		Long bookId = 1L;
		BookCouponTemplate bookCouponTemplate = createMockBookCouponTemplate();
		when(
			bookCouponTemplateRepository.findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(bookCouponTemplate));

		GetBookCouponTemplateAllResponse response = bookCouponTemplateService.findAllByBookId(bookId, true);

		assertNotNull(response);
		assertEquals(1, response.responseList().size());
		verify(bookCouponTemplateRepository, times(1))
			.findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
				anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("모든 도서 ID로 도서 쿠폰 템플릿 조회")
	void findAllByBookId_All() {
		Long bookId = 1L;
		BookCouponTemplate bookCouponTemplate = createMockBookCouponTemplate();
		when(bookCouponTemplateRepository.findAllByBookId(anyLong())).thenReturn(List.of(bookCouponTemplate));

		GetBookCouponTemplateAllResponse response = bookCouponTemplateService.findAllByBookId(bookId, false);

		assertNotNull(response);
		assertEquals(1, response.responseList().size());
		verify(bookCouponTemplateRepository, times(1)).findAllByBookId(anyLong());
	}

	private BookCouponTemplate createMockBookCouponTemplate() {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Sample Coupon")
			.type(CouponType.BOOK)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();

		return BookCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.bookId(1L)
			.build();
	}
}
