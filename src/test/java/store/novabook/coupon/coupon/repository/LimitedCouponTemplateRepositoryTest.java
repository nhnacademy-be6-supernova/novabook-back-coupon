package store.novabook.coupon.coupon.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.LimitedCouponTemplate;

@ActiveProfiles("local")
@DataJpaTest
class LimitedCouponTemplateRepositoryTest {

	@Autowired
	private LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	@BeforeEach
	void setUp() {
		createLimitedCouponTemplate(5L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(10));
	}

	private LimitedCouponTemplate createLimitedCouponTemplate(Long quantity, LocalDateTime startedAt,
		LocalDateTime expirationAt) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon Template")
			.type(CouponType.LIMITED)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(startedAt)
			.expirationAt(expirationAt)
			.usePeriod(30)
			.build();

		couponTemplateRepository.save(couponTemplate);

		LimitedCouponTemplate limitedCouponTemplate = LimitedCouponTemplate.builder()
			.couponTemplate(couponTemplate)
			.quantity(quantity)
			.build();

		return limitedCouponTemplateRepository.save(limitedCouponTemplate);
	}

	@Test
	@DisplayName("쿠폰 템플릿 만료 날짜 이후, 시작 날짜 이전, 수량이 특정 수보다 큰 모든 LimitedCouponTemplate 조회")
	void testFindAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<LimitedCouponTemplate> result = limitedCouponTemplateRepository
			.findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
				LocalDateTime.now(), LocalDateTime.now().minusDays(1), 1L, pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("모든 LimitedCouponTemplate 조회")
	void testFindAll() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<LimitedCouponTemplate> result = limitedCouponTemplateRepository.findAll(pageable);

		assertThat(result.getContent()).isNotEmpty();
	}
}
