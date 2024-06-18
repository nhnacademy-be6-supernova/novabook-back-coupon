package store.novabook.coupon.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.CategoryCoupon;

public interface CategoryCouponRepository extends JpaRepository<CategoryCoupon, String> {
}
