package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import reactor.util.annotation.NonNull;
import store.novabook.coupon.coupon.entity.LimitedCouponTemplate;

/**
 * {@code BookCouponTemplateRepository} 인터페이스는 도서 쿠폰 템플릿에 대한 데이터베이스 작업을 처리합니다.
 */
public interface LimitedCouponTemplateRepository extends JpaRepository<LimitedCouponTemplate, Long> {

	Page<LimitedCouponTemplate> findAllByCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBeforeAndQuantityGreaterThan(
		@NotNull LocalDateTime expirationAt, @NotNull LocalDateTime startedAt, @NotNull Long quantity,
		Pageable pageable);

	@NonNull
	@EntityGraph(attributePaths = {"couponTemplate"})
	Page<LimitedCouponTemplate> findAll(@NonNull Pageable pageable);
}
