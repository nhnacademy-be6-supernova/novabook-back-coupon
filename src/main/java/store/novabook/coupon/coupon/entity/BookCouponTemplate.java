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
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;

/**
 * {@code BookCouponTemplate} 엔티티는 도서 쿠폰 템플릿을 나타냅니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BookCouponTemplate {

	@Id
	private Long id;

	@MapsId
	@ManyToOne
	@JoinColumn(name = "coupon_template_id")
	@NotNull
	private CouponTemplate couponTemplate;

	@NotNull
	private Long bookId;

	/**
	 * {@code BookCouponTemplate} 생성자.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @param bookId         도서 ID
	 */
	@Builder
	public BookCouponTemplate(CouponTemplate couponTemplate, Long bookId) {
		this.couponTemplate = couponTemplate;
		this.bookId = bookId;
	}

	/**
	 * 주어진 요청으로부터 {@code BookCouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 도서 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code BookCouponTemplate} 객체
	 */
	public static BookCouponTemplate of(CreateBookCouponTemplateRequest request) {
		return BookCouponTemplate.builder()
			.couponTemplate(CouponTemplate.of(request))
			.bookId(request.bookId())
			.build();
	}
}
