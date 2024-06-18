package store.novabook.coupon.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.BookCoupon;

public interface BookCouponRepository extends JpaRepository<BookCoupon, String> {
	Page<BookCoupon> findAll(Pageable pageable);
}
