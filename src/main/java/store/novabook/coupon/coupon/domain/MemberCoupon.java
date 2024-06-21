package store.novabook.coupon.coupon.domain;

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
public class MemberCoupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "coupon_code")
	@NotNull
	private Coupon coupon;

	@NotNull
	private Long memberId;

	@NotNull
	@Enumerated(EnumType.STRING)
	private MemberCouponStatus status;

	@NotNull
	private LocalDateTime expirationAt;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public MemberCoupon(Coupon coupon, Long memberId, MemberCouponStatus status, LocalDateTime expirationAt) {
		this.coupon = coupon;
		this.memberId = memberId;
		this.status = status;
		this.expirationAt = expirationAt;
	}

	public static MemberCoupon of(Long memberId, Coupon coupon, MemberCouponStatus status, LocalDateTime expirationAt) {
		return MemberCoupon.builder()
			.coupon(coupon)
			.memberId(memberId)
			.status(status)
			.expirationAt(expirationAt)
			.build();
	}

	public void updateStatus(MemberCouponStatus status) {
		this.status = status;
	}

}