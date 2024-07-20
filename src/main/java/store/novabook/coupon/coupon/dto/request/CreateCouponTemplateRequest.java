package store.novabook.coupon.coupon.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import store.novabook.coupon.common.validator.ValidDiscount;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.DiscountType;
import store.novabook.coupon.coupon.entity.Discountable;

/**
 * {@code CreateCouponTemplateRequest} 레코드는 쿠폰 템플릿 생성 요청을 나타냅니다.
 *
 * @param name              쿠폰 이름
 * @param type              쿠폰 타입
 * @param discountAmount    할인 금액
 * @param discountType      할인 유형
 * @param maxDiscountAmount 최대 할인 금액
 * @param minPurchaseAmount 최소 구매 금액
 * @param startedAt         쿠폰 정책 시작 날짜
 * @param expirationAt      쿠폰 정책 만료 날짜
 * @param usePeriod         사용 가능 기간
 */
@ValidDiscount
public record CreateCouponTemplateRequest(
	@NotNull(message = "이름은 필수 입력 항목입니다.") @NotBlank(message = "이름은 필수 입력 항목입니다.") @Size(max = 255, message = "이름은 255자 이하로 입력해야 합니다.") String name,
	@NotNull(message = "쿠폰 타입은 필수 입력 항목입니다.") CouponType type,
	@NotNull(message = "할인 금액은 필수 입력 항목입니다.") @Min(value = 0, message = "할인 금액은 0 이상이어야 합니다.") Long discountAmount,
	@NotNull(message = "할인 유형은 필수 입력 항목입니다.") DiscountType discountType,
	@NotNull(message = "최대 할인 금액은 필수 입력 항목입니다.") @Min(value = 0, message = "최대 할인 금액은 0 이상이어야 합니다.") Long maxDiscountAmount,
	@NotNull(message = "최소 구매 금액은 필수 입력 항목입니다.") @Min(value = 0, message = "최소 구매 금액은 0 이상이어야 합니다.") Long minPurchaseAmount,
	@NotNull(message = "쿠폰 정책 시작 날짜는 필수 입력 항목입니다.") LocalDateTime startedAt,
	@NotNull(message = "쿠폰 정책 만료 날짜는 필수 입력 항목입니다.") LocalDateTime expirationAt,
	@NotNull(message = "사용 가능 기간은 필수 입력 항목입니다.") Integer usePeriod) implements Discountable {

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
