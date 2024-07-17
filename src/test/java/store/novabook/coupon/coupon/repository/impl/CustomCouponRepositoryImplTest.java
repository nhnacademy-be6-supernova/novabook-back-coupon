package store.novabook.coupon.coupon.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.repository.BookCouponTemplateRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponTemplateRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;

@DataJpaTest
@Import(CustomCouponRepositoryImpl.class)
@ActiveProfiles("local")
class CustomCouponRepositoryImplTest {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	@Autowired
	private CategoryCouponTemplateRepository categoryCouponTemplateRepository;

	@Autowired
	private BookCouponTemplateRepository bookCouponTemplateRepository;

	@Autowired
	private CustomCouponRepositoryImpl customCouponRepositoryImpl;

	private CouponTemplate categoryCouponTemplate;
	private CouponTemplate bookCouponTemplate;
	private CouponTemplate generalCouponTemplate;

	@BeforeEach
	void setUp() {
		categoryCouponTemplate = createCouponTemplate(CouponType.CATEGORY);
		bookCouponTemplate = createCouponTemplate(CouponType.BOOK);
		generalCouponTemplate = createCouponTemplate(CouponType.GENERAL);

		createCategoryCouponTemplate(categoryCouponTemplate, 1L);
		createBookCouponTemplate(bookCouponTemplate, 1L);

		createCoupon(categoryCouponTemplate);
		createCoupon(bookCouponTemplate);
		createCoupon(generalCouponTemplate);
	}

	private CouponTemplate createCouponTemplate(CouponType type) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon Template")
			.type(type)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();

		return couponTemplateRepository.save(couponTemplate);
	}

	private void createCategoryCouponTemplate(CouponTemplate couponTemplate, Long categoryId) {
		CategoryCouponTemplate test = CategoryCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.categoryId(categoryId)
			.build();

		categoryCouponTemplateRepository.save(test);
	}

	private void createBookCouponTemplate(CouponTemplate couponTemplate, Long bookId) {
		BookCouponTemplate test = BookCouponTemplate.builder().couponTemplate(couponTemplate).bookId(bookId).build();

		bookCouponTemplateRepository.save(test);
	}

	private void createCoupon(CouponTemplate couponTemplate) {
		Coupon coupon = Coupon.builder()
			.couponTemplate(couponTemplate)
			.status(CouponStatus.UNUSED)
			.expirationAt(LocalDateTime.now().plusDays(10))
			.build();

		couponRepository.save(coupon);
	}

	@Test
	@DisplayName("유효한 모든 쿠폰 조회")
	void testFindSufficientCoupons() {
		GetCouponAllRequest request = new GetCouponAllRequest(List.of(1L, 2L, 3L), Set.of(1L), Set.of(1L));

		List<GetCouponResponse> result = customCouponRepositoryImpl.findSufficientCoupons(request);

		assertThat(result).isNotEmpty().hasSize(3);
	}
}
