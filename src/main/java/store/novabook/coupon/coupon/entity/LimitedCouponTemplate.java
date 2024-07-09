package store.novabook.coupon.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;

/**
 * {@code LimitedCouponTemplate} 엔티티는 도서 쿠폰 템플릿을 나타냅니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LimitedCouponTemplate {

	@Id
	private Long id;

	@MapsId
	@ManyToOne
	@JoinColumn(name = "coupon_template_id")
	@NotNull
	private CouponTemplate couponTemplate;

	@NotNull
	private Long quantity;

	/**
	 * {@code LimitedCouponTemplate} 생성자.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @param quantity       도서 ID
	 */
	@Builder
	public LimitedCouponTemplate(CouponTemplate couponTemplate, Long quantity) {
		this.couponTemplate = couponTemplate;
		this.quantity = quantity;
	}

	public static LimitedCouponTemplate of(CreateLimitedCouponTemplateRequest request) {
		return LimitedCouponTemplate.builder()
			.couponTemplate(CouponTemplate.of(request))
			.quantity(request.quantity())
			.build();
	}

	public void decrementCouponQuantity() {
		quantity--;
	}
}
