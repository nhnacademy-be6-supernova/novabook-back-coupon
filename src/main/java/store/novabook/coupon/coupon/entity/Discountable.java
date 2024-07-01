package store.novabook.coupon.coupon.entity;

/**
 * {@code Discountable} 인터페이스는 할인 가능한 항목에 대한 메서드를 정의합니다.
 */
public interface Discountable {

	/**
	 * 할인 금액을 반환합니다.
	 *
	 * @return 할인 금액
	 */
	long getDiscountAmount();

	/**
	 * 할인 유형을 반환합니다.
	 *
	 * @return 할인 유형
	 */
	DiscountType getDiscountType();
}
