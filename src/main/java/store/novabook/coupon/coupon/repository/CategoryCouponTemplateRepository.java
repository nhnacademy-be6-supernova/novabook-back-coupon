package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;

public interface CategoryCouponTemplateRepository extends JpaRepository<CategoryCouponTemplate, Long> {
	List<CategoryCouponTemplate> findAllByCategoryIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
		@NotNull Long categoryId, @NotNull LocalDateTime expirationAt, @NotNull LocalDateTime startedAt);

	List<CategoryCouponTemplate> findAllByCategoryId(@NotNull Long categoryId);
}
