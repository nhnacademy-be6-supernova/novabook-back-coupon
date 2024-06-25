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

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {
	Page<CouponTemplate> findAllByType(@NotNull CouponType type, Pageable pageable);

	Optional<CouponTemplate> findTopByTypeOrderByCreatedAtDesc(@NotNull CouponType type);

	List<CouponTemplate> findAllByStartedAtAfter(@NotNull LocalDateTime startedAt);
}
