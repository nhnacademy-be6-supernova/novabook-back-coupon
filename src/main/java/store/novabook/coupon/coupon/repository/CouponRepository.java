package store.novabook.coupon.coupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CustomCouponRepository {
	List<Coupon> findAllByIdInAndStatus(List<Long> couponIdList, CouponStatus status);
}
