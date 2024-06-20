package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

	@Query("SELECT mc FROM MemberCoupon mc " + "JOIN mc.coupon c " + "WHERE mc.memberId = :memberId "
		+ "AND c.code LIKE CONCAT(:prefix, '%') " + "AND mc.status = :status " + "AND c.expirationAt < :expirationAt "
		+ "AND c.startedAt > :startedAt")
	List<MemberCoupon> findValidCouponsByStatus(@Param("memberId") Long memberId, @Param("prefix") String prefix,
		@Param("status") MemberCouponStatus status, @Param("expirationAt") LocalDateTime expirationAt,
		@Param("startedAt") LocalDateTime startedAt);

	Page<MemberCoupon> findAllByStatus(MemberCouponStatus status, Pageable pageable);

}
