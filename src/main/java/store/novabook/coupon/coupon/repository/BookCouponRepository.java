package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.domain.BookCoupon;

public interface BookCouponRepository extends JpaRepository<BookCoupon, String> {

	List<BookCoupon> findAllByBookIdAndCouponExpirationAtAfterAndCouponStartedAtBefore(@NotNull Long bookId,
		@NotNull LocalDateTime couponExpirationAt, @NotNull LocalDateTime couponStartedAt);
}
