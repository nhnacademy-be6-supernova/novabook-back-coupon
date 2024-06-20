package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.CategoryCoupon;

public interface CategoryCouponRepository
	extends JpaRepository<CategoryCoupon, String> {
	List<CategoryCoupon> findAllByCategoryIdAndCouponExpirationAtAfter(Long categoryId, LocalDateTime time);
}
