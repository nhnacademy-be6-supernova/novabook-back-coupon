package store.novabook.coupon.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class CategoryCoupon {

	@Id
	@NotNull
	@Size(max = 16)
	private String couponCode;

	@MapsId("code")
	@ManyToOne
	@JoinColumn(name = "coupon_code")
	@NotNull
	private Coupon coupon;

	private Long categoryId;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

}
