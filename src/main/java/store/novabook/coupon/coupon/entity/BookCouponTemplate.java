package store.novabook.coupon.coupon.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BookCouponTemplate {
	@Id
	private Long id;

	@MapsId
	@ManyToOne
	@JoinColumn(name = "coupon_template_id")
	@NotNull
	private CouponTemplate couponTemplate;

	@NotNull
	private Long bookId;

	@Builder
	public BookCouponTemplate(CouponTemplate couponTemplate, Long bookId) {
		this.couponTemplate = couponTemplate;
		this.bookId = bookId;
	}

	public static BookCouponTemplate of(CouponTemplate couponTemplate, Long bookId) {
		return BookCouponTemplate.builder().couponTemplate(couponTemplate).bookId(bookId).build();
	}
}
