package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.entity.BookCouponTemplate;

/**
 * {@code BookCouponTemplateRepository} 인터페이스는 도서 쿠폰 템플릿에 대한 데이터베이스 작업을 처리합니다.
 */
public interface BookCouponTemplateRepository extends JpaRepository<BookCouponTemplate, Long> {

	/**
	 * 주어진 도서 ID와 유효 기간 내에 있는 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param bookId             도서 ID
	 * @param couponExpirationAt 쿠폰 만료 날짜
	 * @param couponStartedAt    쿠폰 시작 날짜
	 * @return 도서 ID와 유효 기간 내에 있는 쿠폰 템플릿 리스트
	 */
	List<BookCouponTemplate> findAllByBookIdAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
		@NotNull Long bookId,
		@NotNull LocalDateTime couponExpirationAt,
		@NotNull LocalDateTime couponStartedAt
	);

	/**
	 * 주어진 도서 ID에 해당하는 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param bookId 도서 ID
	 * @return 도서 ID에 해당하는 쿠폰 템플릿 리스트
	 */
	List<BookCouponTemplate> findAllByBookId(Long bookId);
}
