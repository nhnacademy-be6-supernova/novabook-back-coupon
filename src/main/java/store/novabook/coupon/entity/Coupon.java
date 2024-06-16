package store.novabook.coupon.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coupon {

	public enum DiscountType {
		AMOUNT,
		PERCENT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false)
	private BigDecimal discountAmount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private DiscountType discountType;

	@Column(nullable = false)
	private BigDecimal maxDiscountAmount;

	@Column(nullable = false)
	private BigDecimal minPurchaseAmount;

	@Column(nullable = false)
	private ZonedDateTime startedAt;

	@Column(nullable = false)
	private ZonedDateTime expirationAt;

	@Column(nullable = false)
	private ZonedDateTime createdAt = ZonedDateTime.now();

	@Column
	private ZonedDateTime updatedAt;

}
