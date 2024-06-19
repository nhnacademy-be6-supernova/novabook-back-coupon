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
public class MemberCoupon {

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
	private Long memberId;

	@NotNull
	@Size(max = 8)
	@Enumerated(EnumType.STRING)
	private MemberCouponStatus status;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public MemberCoupon(String couponCode, Coupon coupon, Long memberId, MemberCouponStatus status) {
		this.couponCode = couponCode;
		this.coupon = coupon;
		this.memberId = memberId;
		this.status = status;
	}

	public static MemberCoupon of(Long memberId, Coupon coupon, MemberCouponStatus status) {
		return MemberCoupon.builder()
			.couponCode(coupon.getCode())
			.coupon(coupon)
			.memberId(memberId)
			.status(status)
			.build();
	}

}