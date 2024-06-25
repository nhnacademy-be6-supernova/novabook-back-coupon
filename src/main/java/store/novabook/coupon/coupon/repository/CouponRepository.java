package store.novabook.coupon.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
