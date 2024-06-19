package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, String> {

	List<MemberCoupon> findAllByMemberIdAndCoupon_CodeStartsWithAndStatusMatchesAndCoupon_ExpirationAtBeforeAndCoupon_StartedAtAfter(@NotNull Long memberId, @Size(max = 16) @NotNull String coupon_code, @NotNull MemberCouponStatus status, @NotNull LocalDateTime coupon_expirationAt, @NotNull LocalDateTime coupon_startedAt);


}
