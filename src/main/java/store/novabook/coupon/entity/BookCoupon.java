package store.novabook.coupon.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCoupon {
	@Id
	@NotNull
	@Size(max = 16)
	private String couponCode;

	@MapsId("code")
	@ManyToOne
	@JoinColumn(name = "coupon_code")
	@NotNull
	private Coupon coupon;

	@NotNull
	private Long bookId;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
