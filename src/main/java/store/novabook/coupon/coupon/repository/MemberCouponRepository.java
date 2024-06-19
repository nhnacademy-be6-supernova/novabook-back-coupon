package store.novabook.coupon.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.MemberCoupon;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, String> {
}
