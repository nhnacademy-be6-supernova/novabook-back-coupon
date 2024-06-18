package store.novabook.coupon.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, String> {
	Page<Coupon> findAllByCodeStartsWith(String string, Pageable pageable);
}
