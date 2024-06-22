package store.novabook.coupon.coupon.domain;

public enum CouponType {
	GENERAL("G"),
	BIRTHDAY("H"),
	WELCOME("W"),
	BOOK("B"),
	CATEGORY("C");

	private final String prefix;

	CouponType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}