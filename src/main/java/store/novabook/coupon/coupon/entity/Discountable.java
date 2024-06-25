package store.novabook.coupon.coupon.entity;

public interface Discountable {
	long getDiscountAmount();

	DiscountType getDiscountType();
}
