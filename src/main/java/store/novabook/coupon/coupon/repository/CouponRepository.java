package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, String> {
	Page<Coupon> findAllByCodeStartsWith(String string, Pageable pageable);

	List<Coupon> findAllByExpirationAtBefore(@NotNull LocalDateTime expirationAt);
}
