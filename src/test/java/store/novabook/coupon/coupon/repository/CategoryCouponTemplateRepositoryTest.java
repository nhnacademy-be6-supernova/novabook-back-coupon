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

import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@DataJpaTest
@ActiveProfiles("local")
class CategoryCouponTemplateRepositoryTest {

	@Autowired
	private CategoryCouponTemplateRepository repository;

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	private CategoryCouponTemplate createCategoryCouponTemplate(Long categoryId, LocalDateTime startedAt,
		LocalDateTime expirationAt) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon")
			.type(CouponType.CATEGORY)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(startedAt)
			.expirationAt(expirationAt)
			.usePeriod(30)
			.build();

		couponTemplateRepository.save(couponTemplate);

		CategoryCouponTemplate categoryCouponTemplate = CategoryCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.categoryId(categoryId)
			.build();

		return repository.save(categoryCouponTemplate);
	}

	@Test
	@DisplayName("카테고리 ID와 유효 기간 내에 있는 모든 쿠폰 템플릿 조회")
	void testFindAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.minusDays(1), now.plusDays(10));

		List<CategoryCouponTemplate> result = repository.findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
			List.of(categoryId), now, now);

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("카테고리 ID와 유효 기간 외에 있는 쿠폰 템플릿 조회")
	void testFindAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore_Empty() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.plusDays(1), now.plusDays(10));

		List<CategoryCouponTemplate> result = repository.findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
			List.of(categoryId), now, now);

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("카테고리 ID에 해당하는 모든 쿠폰 템플릿 조회")
	void testFindAllByCategoryIdIn() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.minusDays(1), now.plusDays(10));

		List<CategoryCouponTemplate> result = repository.findAllByCategoryIdIn(List.of(categoryId));

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("카테고리 ID에 해당하는 쿠폰 템플릿이 없는 경우")
	void testFindAllByCategoryIdIn_Empty() {
		Long categoryId = 1L;

		List<CategoryCouponTemplate> result = repository.findAllByCategoryIdIn(List.of(categoryId));

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("모든 카테고리 쿠폰 템플릿 페이지 조회")
	void testFindAllWithPageable() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.minusDays(1), now.plusDays(10));

		Pageable pageable = PageRequest.of(0, 10);
		Page<CategoryCouponTemplate> result = repository.findAll(pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("카테고리 쿠폰 템플릿이 없는 경우 페이지 조회")
	void testFindAllWithPageable_Empty() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CategoryCouponTemplate> result = repository.findAll(pageable);

		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("유효한 카테고리 쿠폰 템플릿 페이지 조회")
	void testFindAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.minusDays(1), now.plusDays(10));

		Pageable pageable = PageRequest.of(0, 10);
		Page<CategoryCouponTemplate> result = repository.findAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter(
			now, now, pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("유효한 카테고리 쿠폰 템플릿이 없는 경우 페이지 조회")
	void testFindAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter_Empty() {
		Long categoryId = 1L;
		LocalDateTime now = LocalDateTime.now();
		createCategoryCouponTemplate(categoryId, now.plusDays(1), now.plusDays(10));

		Pageable pageable = PageRequest.of(0, 10);
		Page<CategoryCouponTemplate> result = repository.findAllByCouponTemplateStartedAtBeforeAndCouponTemplateExpirationAtAfter(
			now, now, pageable);

		assertThat(result.getContent()).isEmpty();
	}
}
