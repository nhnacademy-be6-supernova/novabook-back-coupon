package store.novabook.coupon.coupon.dto.request;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.common.validator.ValidDiscount;
import store.novabook.coupon.coupon.domain.DiscountType;
import store.novabook.coupon.coupon.domain.Discountable;

@ValidDiscount
public record CreateCouponCategoryRequest(
	@NotNull(message = "카테고리 ID는 필수 입력 항목입니다.") Long categoryId,
	@NotNull(message = "이름은 필수 입력 항목입니다.") @Size(max = 255, message = "이름은 255자 이하로 입력해야 합니다.") String name,
	@NotNull(message = "할인 금액은 필수 입력 항목입니다.") @Range(min = 0, max = 100000) long discountAmount,
	@NotNull(message = "할인 유형은 필수 입력 항목입니다.") DiscountType discountType,
	@NotNull(message = "최대 할인 금액은 필수 입력 항목입니다.") Long maxDiscountAmount,
	@NotNull(message = "최소 구매 금액은 필수 입력 항목입니다.") Long minPurchaseAmount,
	@NotNull(message = "시작 날짜는 필수 입력 항목입니다.") LocalDateTime startedAt,
	@NotNull(message = "만료 날짜는 필수 입력 항목입니다.") LocalDateTime expirationAt,
	@NotNull(message = "사용 가능일은 필수 입력 항목입니다.") Integer usePeriod)
	implements Discountable {
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