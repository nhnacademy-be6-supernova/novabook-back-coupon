package store.novabook.coupon.coupon.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.coupon.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, String> {
	Page<Coupon> findAllByCodeStartsWith(String string, Pageable pageable);

	Page<Coupon> findAllByCodeIsNotLikeAndCodeIsNotLike(@Size(max = 16) @NotNull String prefix1,
		@Size(max = 16) @NotNull String prefix2, Pageable pageable);

	Optional<Coupon> findTopByCodeStartsWithOrderByCreatedAtDesc(@Size(max = 16) @NotNull String prefix);
}
