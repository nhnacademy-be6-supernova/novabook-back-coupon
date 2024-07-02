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

/**
 * {@code CategoryCouponTemplate} 엔티티는 카테고리 쿠폰 템플릿을 나타냅니다.
 */
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

	/**
	 * {@code CategoryCouponTemplate} 생성자.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @param categoryId     카테고리 ID
	 */
	@Builder
	public CategoryCouponTemplate(CouponTemplate couponTemplate, Long categoryId) {
		this.id = couponTemplate.getId();
		this.couponTemplate = couponTemplate;
		this.categoryId = categoryId;
	}

	/**
	 * 주어진 요청으로부터 {@code CategoryCouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code CategoryCouponTemplate} 객체
	 */
	public static CategoryCouponTemplate of(CreateCategoryCouponTemplateRequest request) {
		return CategoryCouponTemplate.builder()
			.couponTemplate(CouponTemplate.of(request))
			.categoryId(request.categoryId())
			.build();
	}
}
