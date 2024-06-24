package store.novabook.coupon.coupon.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CategoryCouponTemplate {
	@Id
	private Long id;

	@MapsId
	@ManyToOne
	@JoinColumn(name = "coupon_template_id")
	@NotNull
	private CouponTemplate couponTemplate;

	@NotNull
	private Long categoryId;

	@Builder
	public CategoryCouponTemplate(CouponTemplate couponTemplate, Long categoryId) {
		this.id = couponTemplate.getId();
		this.couponTemplate = couponTemplate;
		this.categoryId = categoryId;
	}

	public static CategoryCouponTemplate of(CreateCategoryCouponTemplateRequest request) {
		return CategoryCouponTemplate.builder()
			.couponTemplate(CouponTemplate.of(request))
			.categoryId(request.categoryId())
			.build();
	}
}
