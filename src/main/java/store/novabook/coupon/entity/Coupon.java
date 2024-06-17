package store.novabook.coupon.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

	public enum DiscountType {
		AMOUNT,
		PERCENT
	}

	@Id
	@Size(max = 16)
	@NotNull
	private String code;

	@NotNull
	@Size(max = 255)
	private String name;

	@NotNull
	private BigDecimal discountAmount;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Size(max = 10)
	private DiscountType discountType;

	@NotNull
	private BigDecimal maxDiscountAmount;

	@NotNull
	private BigDecimal minPurchaseAmount;

	@NotNull
	private LocalDateTime startedAt;

	@NotNull
	private LocalDateTime expirationAt;

	@NotNull
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime updatedAt;

}
