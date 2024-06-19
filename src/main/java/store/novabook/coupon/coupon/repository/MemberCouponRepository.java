package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, String> {

	List<MemberCoupon> findAllByMemberIdAndCoupon_CodeStartsWithAndStatusMatchesAndCoupon_ExpirationAtBeforeAndCoupon_StartedAtAfter(
		@NotNull Long memberId, @Size(max = 16) @NotNull String couponCode, @NotNull MemberCouponStatus status,
		@NotNull LocalDateTime expirationAt, @NotNull LocalDateTime startedAt);

	Page<MemberCoupon> findAllByStatus(MemberCouponStatus status, Pageable pageable);
}
