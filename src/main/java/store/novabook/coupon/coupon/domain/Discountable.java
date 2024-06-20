package store.novabook.coupon.coupon.domain;


import store.novabook.coupon.coupon.domain.DiscountType;

import java.time.LocalDateTime;

public interface Discountable {
    long getDiscountAmount();
    DiscountType getDiscountType();
}
