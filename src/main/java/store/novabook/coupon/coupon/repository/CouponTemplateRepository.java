package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;

/**
 * {@code CouponTemplateRepository} 인터페이스는 쿠폰 템플릿에 대한 데이터베이스 작업을 처리합니다.
 */
public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {

	/**
	 * 주어진 쿠폰 타입에 해당하는 모든 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param type     쿠폰 타입
	 * @param pageable 페이지 정보
	 * @return 쿠폰 타입에 해당하는 쿠폰 템플릿의 페이지
	 */
	Page<CouponTemplate> findAllByType(@NotNull CouponType type, Pageable pageable);

	/**
	 * 주어진 쿠폰 타입과 시작 날짜 및 만료 날짜 사이에 해당하는 모든 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param type     쿠폰 타입
	 * @param start    시작 날짜
	 * @param end      만료 날짜
	 * @param pageable 페이지 정보
	 * @return 쿠폰 타입과 날짜 범위에 해당하는 쿠폰 템플릿의 페이지
	 */
	Page<CouponTemplate> findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(
		@NotNull CouponType type,
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	);

	/**
	 * 주어진 시작 날짜 이후에 시작된 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param startedAt 시작 날짜
	 * @return 시작 날짜 이후에 시작된 모든 쿠폰 템플릿의 리스트
	 */
	List<CouponTemplate> findAllByStartedAtAfter(@NotNull LocalDateTime startedAt);

	/**
	 * 주어진 쿠폰 타입에 해당하는 가장 최근에 생성된 쿠폰 템플릿을 조회합니다.
	 *
	 * @param type 쿠폰 타입
	 * @return 가장 최근에 생성된 쿠폰 템플릿
	 */
	Optional<CouponTemplate> findTopByTypeOrderByCreatedAtDesc(@NotNull CouponType type);
}
