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

import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;

@ActiveProfiles("local")
@DataJpaTest
class CouponRepositoryTest {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponTemplateRepository couponTemplateRepository;

	private Coupon createCoupon(Long templateId, CouponStatus status, LocalDateTime expirationAt) {
		CouponTemplate couponTemplate = CouponTemplate.builder()
			.name("Test Coupon Template")
			.type(CouponType.GENERAL)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.maxDiscountAmount(5000L)
			.minPurchaseAmount(10000L)
			.startedAt(LocalDateTime.now().minusDays(1))
			.expirationAt(LocalDateTime.now().plusDays(10))
			.usePeriod(30)
			.build();

		couponTemplateRepository.save(couponTemplate);

		return Coupon.builder().couponTemplate(couponTemplate).status(status).expirationAt(expirationAt).build();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 상태에 해당하는 모든 쿠폰 조회")
	void testFindAllByIdInAndStatus() {
		Coupon coupon = createCoupon(1L, CouponStatus.UNUSED, LocalDateTime.now().plusDays(1));
		couponRepository.save(coupon);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Coupon> result = couponRepository.findAllByIdInAndStatus(List.of(coupon.getId()), CouponStatus.UNUSED,
			pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 상태에 해당하는 모든 쿠폰 조회 - 빈 결과")
	void testFindAllByIdInAndStatus_Empty() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Coupon> result = couponRepository.findAllByIdInAndStatus(List.of(1L), CouponStatus.USED, pageable);

		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 상태 및 만료 날짜 이후의 모든 쿠폰 조회")
	void testFindAllByIdInAndStatusAndExpirationAtAfter() {
		Coupon coupon = createCoupon(1L, CouponStatus.UNUSED, LocalDateTime.now().plusDays(1));
		couponRepository.save(coupon);

		List<Coupon> result = couponRepository.findAllByIdInAndStatusAndExpirationAtAfter(List.of(coupon.getId()),
			CouponStatus.UNUSED, LocalDateTime.now());

		assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 상태 및 만료 날짜 이후의 모든 쿠폰 조회 - 빈 결과")
	void testFindAllByIdInAndStatusAndExpirationAtAfter_Empty() {
		List<Coupon> result = couponRepository.findAllByIdInAndStatusAndExpirationAtAfter(List.of(1L),
			CouponStatus.USED, LocalDateTime.now().plusDays(1));

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트에 해당하는 모든 쿠폰 조회")
	void testFindAllByIdIn() {
		Coupon coupon = createCoupon(1L, CouponStatus.UNUSED, LocalDateTime.now().plusDays(1));
		couponRepository.save(coupon);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Coupon> result = couponRepository.findAllByIdIn(List.of(coupon.getId()), pageable);

		assertThat(result.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트에 해당하는 모든 쿠폰 조회 - 빈 결과")
	void testFindAllByIdIn_Empty() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Coupon> result = couponRepository.findAllByIdIn(List.of(1L), pageable);

		assertThat(result.getContent()).isEmpty();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 쿠폰 템플릿 ID로 쿠폰 존재 여부 확인")
	void testExistsByIdInAndCouponTemplateId() {
		Coupon coupon = createCoupon(1L, CouponStatus.UNUSED, LocalDateTime.now().plusDays(1));
		couponRepository.save(coupon);

		boolean exists = couponRepository.existsByIdInAndCouponTemplateId(List.of(coupon.getId()),
			coupon.getCouponTemplate().getId());

		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("쿠폰 ID 리스트와 쿠폰 템플릿 ID로 쿠폰 존재 여부 확인 - 존재하지 않음")
	void testExistsByIdInAndCouponTemplateId_False() {
		boolean exists = couponRepository.existsByIdInAndCouponTemplateId(List.of(1L), 1L);

		assertThat(exists).isFalse();
	}
}
