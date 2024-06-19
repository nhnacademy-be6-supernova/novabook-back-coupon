package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.MemberCoupon;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, String> {

	List<MemberCoupon> findAllByMemberIdAndCoupon_ExpirationAtBefore(Long memberId, LocalDateTime time);

}
