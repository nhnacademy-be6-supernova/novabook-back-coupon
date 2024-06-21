package store.novabook.coupon.coupon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.repository.querydsl.CustomizedMemberCouponRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, CustomizedMemberCouponRepository {
	Page<MemberCoupon> findAllByStatus(MemberCouponStatus status, Pageable pageable);

	void deleteByCouponCode(@Size(max = 16) @NotNull String couponCode);
}
