package store.novabook.coupon.coupon.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCouponBookRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponCategoryRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Coupon {

	@Id
	@Size(max = 16)
	@NotNull
	private String code;

	@NotNull
	@Size(max = 255)
	private String name;

	@NotNull
	private long discountAmount;

	@Enumerated(EnumType.STRING)
	@NotNull
	private DiscountType discountType;

	@NotNull
	private long maxDiscountAmount;

	@NotNull
	@Min(0)
	private long minPurchaseAmount;

	@NotNull
	private LocalDateTime startedAt;

	@NotNull
	private LocalDateTime expirationAt;

	@CreatedDate
	@NotNull
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public Coupon(
		String code,
		String name,
		long discountAmount,
		DiscountType discountType,
		long maxDiscountAmount,
		long minPurchaseAmount,
		LocalDateTime startedAt,
		LocalDateTime expirationAt
	) {
		this.code = code;
		this.name = name;
		this.discountAmount = discountAmount;
		this.discountType = discountType;
		this.maxDiscountAmount = maxDiscountAmount;
		this.minPurchaseAmount = minPurchaseAmount;
		this.startedAt = startedAt;
		this.expirationAt = expirationAt;
	}

	public static Coupon of(String code, CreateCouponRequest request) {
		return Coupon.builder()
			.code(code)
			.name(request.name())
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.build();
	}

	public static Coupon of(String code, CreateCouponBookRequest request) {
		return Coupon.builder()
			.code(code)
			.name(request.name())
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.build();
	}

	public static Coupon of(String code, CreateCouponCategoryRequest request) {
		return Coupon.builder()
			.code(code)
			.name(request.name())
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.build();
	}

	public void updateExprationAt(LocalDateTime expirationAt) {
		this.expirationAt = expirationAt;
	}

}
