package store.novabook.coupon.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.repository.querydsl.CustomizedMemberCouponRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, CustomizedMemberCouponRepository {
	Page<MemberCoupon> findAllByStatus(MemberCouponStatus status, Pageable pageable);

}
