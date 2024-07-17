package store.novabook.coupon.coupon.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@DataJpaTest
@ActiveProfiles("test")
class BookCouponTemplateRepositoryTest {

	@Autowired
	private BookCouponTemplateRepository repository;

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	private BookCouponTemplate createBookCouponTemplate(Long bookId, LocalDateTime startedAt,
		LocalDateTime expirationAt) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon")
			.type(CouponType.BOOK)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(startedAt)
			.expirationAt(expirationAt)
			.usePeriod(30)
			.build();

		couponTemplateRepository.save(couponTemplate);

		BookCouponTemplate bookCouponTemplate = BookCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.bookId(bookId)
			.build();

		return repository.save(bookCouponTemplate);
	}

	@Test
	@DisplayName("도서 ID와 유효 기간 내에 있는 모든 쿠폰 템플릿 조회")
	void testFindAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore() {
		Long bookId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createBookCouponTemplate(bookId, now.minusDays(1), now.plusDays(10));

		List<BookCouponTemplate> result = repository.findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
			bookId, now, now);

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("도서 ID와 유효 기간 외에 있는 쿠폰 템플릿 조회")
	void testFindAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore_Empty() {
		Long bookId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createBookCouponTemplate(bookId, now.plusDays(1), now.plusDays(10));

		List<BookCouponTemplate> result = repository.findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
			bookId, now, now);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("도서 ID에 해당하는 모든 쿠폰 템플릿 조회")
	void testFindAllByBookId() {
		Long bookId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createBookCouponTemplate(bookId, now.minusDays(1), now.plusDays(10));

		List<BookCouponTemplate> result = repository.findAllByBookId(bookId);

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("도서 ID에 해당하는 쿠폰 템플릿이 없는 경우")
	void testFindAllByBookId_Empty() {
		Long bookId = 1L;

		List<BookCouponTemplate> result = repository.findAllByBookId(bookId);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("모든 쿠폰 템플릿 페이지 조회")
	void testFindAllWithPageable() {
		Long bookId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createBookCouponTemplate(bookId, now.minusDays(1), now.plusDays(10));

		Pageable pageable = PageRequest.of(0, 10);
		Page<BookCouponTemplate> result = repository.findAll(pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 템플릿이 없는 경우 페이지 조회")
	void testFindAllWithPageable_Empty() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<BookCouponTemplate> result = repository.findAll(pageable);

		assertThat(result.getContent()).isEmpty();
	}
}
