package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.domain.CategoryCoupon;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, String> {
	List<CategoryCoupon> findAllByCategoryIdAndCouponExpirationAtAfterAndCouponStartedAtBefore(@NotNull Long categoryId,
		@NotNull LocalDateTime expirationAt, @NotNull LocalDateTime startedAt);
}
