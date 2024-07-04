package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;

/**
 * {@code CouponRepository} 인터페이스는 쿠폰 엔티티에 대한 데이터베이스 작업을 처리합니다.
 * 또한 사용자 정의 메서드를 포함합니다.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long>, CustomCouponRepository {

	/**
	 * 주어진 쿠폰 ID 리스트와 상태에 해당하는 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @param status       쿠폰 상태
	 * @return 쿠폰 ID 리스트와 상태에 해당하는 쿠폰 리스트
	 */
	List<Coupon> findAllByIdInAndStatus(List<Long> couponIdList, CouponStatus status);

	List<Coupon> findAllByIdInAndStatusAndExpirationAtAfter(List<Long> couponIdList, CouponStatus status,
		LocalDateTime expirationAt);
}
