package store.novabook.coupon.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	Page<Coupon> findAllByStatus(CouponStatus status, Pageable pageable);
}
