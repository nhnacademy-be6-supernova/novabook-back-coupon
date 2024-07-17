package store.novabook.coupon.coupon.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

@ActiveProfiles("local")
@DataJpaTest
class CouponTemplateRepositoryTest {

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	@BeforeEach
	void setUp() {
		createCouponTemplate(CouponType.GENERAL, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(10));
	}

	private CouponTemplate createCouponTemplate(CouponType type, LocalDateTime startedAt, LocalDateTime expirationAt) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon Template")
			.type(type)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(startedAt)
			.expirationAt(expirationAt)
			.usePeriod(30)
			.build();

		return couponTemplateRepository.save(couponTemplate);
	}

	@Test
	@DisplayName("쿠폰 타입에 해당하는 모든 쿠폰 템플릿 조회")
	void testFindAllByType() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CouponTemplate> result = couponTemplateRepository.findAllByType(CouponType.GENERAL, pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 타입과 날짜 범위에 해당하는 모든 쿠폰 템플릿 조회")
	void testFindAllByTypeAndStartedAtBeforeAndExpirationAtAfter() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CouponTemplate> result = couponTemplateRepository.findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(
			CouponType.GENERAL, LocalDateTime.now(), LocalDateTime.now(), pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 타입과 날짜 범위에 해당하는 모든 쿠폰 템플릿 조회 - 빈 결과")
	void testFindAllByTypeAndStartedAtBeforeAndExpirationAtAfter_Empty() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<CouponTemplate> result = couponTemplateRepository.findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(
			CouponType.GENERAL, LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(20), pageable);

		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("시작 날짜 이후에 시작된 모든 쿠폰 템플릿 조회")
	void testFindAllByStartedAtAfter() {
		List<CouponTemplate> result = couponTemplateRepository.findAllByStartedAtAfter(
			LocalDateTime.now().minusDays(2));

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("시작 날짜 이후에 시작된 모든 쿠폰 템플릿 조회 - 빈 결과")
	void testFindAllByStartedAtAfter_Empty() {
		List<CouponTemplate> result = couponTemplateRepository.findAllByStartedAtAfter(
			LocalDateTime.now().plusDays(10));

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("가장 최근에 생성된 쿠폰 템플릿 조회")
	void testFindTopByTypeOrderByCreatedAtDesc() {
		Optional<CouponTemplate> result = couponTemplateRepository.findTopByTypeOrderByCreatedAtDesc(
			CouponType.GENERAL);

		assertThat(result).isPresent();
	}

	@Test
	@DisplayName("가장 최근에 생성된 쿠폰 템플릿 조회 - 빈 결과")
	void testFindTopByTypeOrderByCreatedAtDesc_Empty() {
		Optional<CouponTemplate> result = couponTemplateRepository.findTopByTypeOrderByCreatedAtDesc(
			CouponType.BIRTHDAY);

		assertThat(result).isNotPresent();
	}
}
