package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;

public interface BookCouponTemplateRepository extends JpaRepository<BookCouponTemplate, Long> {

	List<BookCouponTemplate> findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
		@NotNull Long bookId, @NotNull LocalDateTime couponExpirationAt, @NotNull LocalDateTime couponStartedAt);

	List<BookCouponTemplate> findAllByBookId(Long bookId);
}
