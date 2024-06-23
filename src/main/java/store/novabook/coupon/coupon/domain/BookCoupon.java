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
public class BookCoupon {
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
	private Long bookId;

	@Builder
	public BookCoupon(Coupon coupon, Long bookId) {
		this.couponCode = coupon.getCode();
		this.coupon = coupon;
		this.bookId = bookId;
	}

	public static BookCoupon of(Coupon coupon, Long bookId) {
		return BookCoupon.builder()
			.coupon(coupon)
			.bookId(bookId)
			.build();
	}
}
