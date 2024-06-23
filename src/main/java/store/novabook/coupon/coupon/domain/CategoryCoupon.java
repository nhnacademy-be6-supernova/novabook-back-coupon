package store.novabook.coupon.coupon.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CategoryCoupon {

	@Id
	@NotNull
	@Size(max = 16)
	private String couponCode;

	@MapsId
	@ManyToOne
	@JoinColumn(name = "coupon_code")
	@NotNull
	private Coupon coupon;

	@NotNull
	private Long categoryId;

	@Builder
	public CategoryCoupon(Coupon coupon, Long categoryId) {
		this.couponCode = coupon.getCode();
		this.coupon = coupon;
		this.categoryId = categoryId;
	}

	public static CategoryCoupon of(Coupon coupon, Long categoryId) {
		return CategoryCoupon.builder().coupon(coupon).categoryId(categoryId).build();
	}

}
