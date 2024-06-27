package store.novabook.coupon.coupon.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "coupon_template_id")
	@NotNull
	private CouponTemplate couponTemplate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private CouponStatus status;

	@NotNull
	private LocalDateTime expirationAt;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public Coupon(CouponTemplate couponTemplate, CouponStatus status, LocalDateTime expirationAt) {
		this.couponTemplate = couponTemplate;
		this.status = status;
		this.expirationAt = expirationAt;
	}

	public static Coupon of(CouponTemplate couponTemplate, CouponStatus status, LocalDateTime expirationAt) {
		return Coupon.builder().couponTemplate(couponTemplate).status(status).expirationAt(expirationAt).build();
	}

	public void updateStatus(CouponStatus status) {
		this.status = status;
	}

}