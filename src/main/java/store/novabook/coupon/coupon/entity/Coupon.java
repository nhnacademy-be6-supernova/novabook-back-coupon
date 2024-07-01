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

/**
 * {@code Coupon} 엔티티는 쿠폰을 나타냅니다.
 */
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

	/**
	 * {@code Coupon} 생성자.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @param status         쿠폰 상태
	 * @param expirationAt   쿠폰 만료 날짜
	 */
	@Builder
	public Coupon(CouponTemplate couponTemplate, CouponStatus status, LocalDateTime expirationAt) {
		this.couponTemplate = couponTemplate;
		this.status = status;
		this.expirationAt = expirationAt;
	}

	/**
	 * 주어진 쿠폰 템플릿, 상태 및 만료 날짜로부터 {@code Coupon} 객체를 생성합니다.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @param status         쿠폰 상태
	 * @param expirationAt   쿠폰 만료 날짜
	 * @return 생성된 {@code Coupon} 객체
	 */
	public static Coupon of(CouponTemplate couponTemplate, CouponStatus status, LocalDateTime expirationAt) {
		return Coupon.builder()
			.couponTemplate(couponTemplate)
			.status(status)
			.expirationAt(expirationAt)
			.build();
	}

	/**
	 * 쿠폰의 상태를 업데이트합니다.
	 *
	 * @param status 새로운 쿠폰 상태
	 */
	public void updateStatus(CouponStatus status) {
		this.status = status;
	}
}
