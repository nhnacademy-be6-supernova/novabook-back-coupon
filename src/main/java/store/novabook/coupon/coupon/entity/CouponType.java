package store.novabook.coupon.coupon.entity;

/**
 * {@code CouponType} 열거형은 쿠폰의 유형을 나타냅니다.
 */
public enum CouponType {
	/**
	 * 일반 쿠폰 유형.
	 */
	GENERAL,

	/**
	 * 생일 쿠폰 유형.
	 */
	BIRTHDAY,

	/**
	 * 환영 쿠폰 유형.
	 */
	WELCOME,

	/**
	 * 도서 쿠폰 유형.
	 */
	BOOK,

	/**
	 * 카테고리 쿠폰 유형.
	 */
	CATEGORY,

	/**
	 * 선착순 쿠폰 유형.
	 */
	LIMITED;
}
