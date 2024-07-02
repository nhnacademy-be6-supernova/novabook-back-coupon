package store.novabook.coupon.coupon.dto.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.common.validator.ValidDiscount;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.Discountable;

/**
 * {@code CreateCategoryCouponTemplateRequest} 레코드는 카테고리 쿠폰 템플릿 생성 요청을 나타냅니다.
 *
 * @param categoryId        카테고리 ID
 * @param name              쿠폰 이름
 * @param discountAmount    할인 금액
 * @param discountType      할인 유형
 * @param maxDiscountAmount 최대 할인 금액
 * @param minPurchaseAmount 최소 구매 금액
 * @param startedAt         시작 날짜
 * @param expirationAt      만료 날짜
 * @param usePeriod         사용 가능일
 */
@ValidDiscount
public record CreateCategoryCouponTemplateRequest(@NotNull(message = "카테고리 ID는 필수 입력 항목입니다.") Long categoryId,
												  @NotNull(message = "이름은 필수 입력 항목입니다.") @Size(max = 255, message = "이름은 255자 이하로 입력해야 합니다.") String name,
												  @NotNull(message = "할인 금액은 필수 입력 항목입니다.") @Range(min = 0, max = 100000, message = "할인 금액은 0에서 100,000 사이여야 합니다.") long discountAmount,
												  @NotNull(message = "할인 유형은 필수 입력 항목입니다.") DiscountType discountType,
												  @NotNull(message = "최대 할인 금액은 필수 입력 항목입니다.") Long maxDiscountAmount,
												  @NotNull(message = "최소 구매 금액은 필수 입력 항목입니다.") Long minPurchaseAmount,
												  @NotNull(message = "시작 날짜는 필수 입력 항목입니다.") LocalDateTime startedAt,
												  @NotNull(message = "만료 날짜는 필수 입력 항목입니다.") LocalDateTime expirationAt,
												  @NotNull(message = "사용 가능일은 필수 입력 항목입니다.") Integer usePeriod)
	implements Discountable {

	/**
	 * 시작 날짜가 만료 날짜보다 이전인지 확인합니다.
	 *
	 * @return 시작 날짜가 만료 날짜보다 이전인 경우 {@code true}, 그렇지 않은 경우 {@code false}
	 */
	@AssertTrue(message = "시작 날짜는 만료 날짜보다 이전이어야 합니다.")
	public boolean isValidDates() {
		return startedAt.isBefore(expirationAt);
	}

	@Override
	public long getDiscountAmount() {
		return discountAmount;
	}

	@Override
	public DiscountType getDiscountType() {
		return discountType;
	}
}
