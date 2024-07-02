package store.novabook.coupon.coupon.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.coupon.coupon.entity.CategoryCouponTemplate;

/**
 * {@code CategoryCouponTemplateRepository} 인터페이스는 카테고리 쿠폰 템플릿에 대한 데이터베이스 작업을 처리합니다.
 */
public interface CategoryCouponTemplateRepository extends JpaRepository<CategoryCouponTemplate, Long> {

	/**
	 * 주어진 카테고리 ID와 유효 기간 내에 있는 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param categoryIdList 카테고리 ID
	 * @param expirationAt   쿠폰 만료 날짜
	 * @param startedAt      쿠폰 시작 날짜
	 * @return 카테고리 ID와 유효 기간 내에 있는 쿠폰 템플릿 리스트
	 */
	List<CategoryCouponTemplate> findAllByCategoryIdInAndCouponTemplateExpirationAtAfterAndCouponTemplateStartedAtBefore(
		@NotNull List<Long> categoryIdList, @NotNull LocalDateTime expirationAt, @NotNull LocalDateTime startedAt);

	/**
	 * 주어진 카테고리 ID에 해당하는 모든 쿠폰 템플릿을 조회합니다.
	 *
	 * @param categoryIdList 카테고리 ID
	 * @return 카테고리 ID에 해당하는 쿠폰 템플릿 리스트
	 */
	List<CategoryCouponTemplate> findAllByCategoryIdIn(@NotNull List<Long> categoryIdList);
}
