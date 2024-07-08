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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.coupon.coupon.dto.request.CreateBookCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.request.CreateCategoryCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.request.CreateLimitedCouponTemplateRequest;

/**
 * {@code CouponTemplate} 엔티티는 쿠폰 템플릿을 나타냅니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class CouponTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 255)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	private CouponType type;

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

	@NotNull
	private int usePeriod;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	/**
	 * {@code CouponTemplate} 생성자.
	 *
	 * @param name              쿠폰 이름
	 * @param type              쿠폰 타입
	 * @param discountAmount    할인 금액
	 * @param discountType      할인 유형
	 * @param maxDiscountAmount 최대 할인 금액
	 * @param minPurchaseAmount 최소 구매 금액
	 * @param startedAt         쿠폰 정책 시작 날짜
	 * @param expirationAt      쿠폰 정책 만료 날짜
	 * @param usePeriod         사용 가능 기간
	 */
	@Builder
	public CouponTemplate(String name, CouponType type, long discountAmount, DiscountType discountType,
		long maxDiscountAmount, long minPurchaseAmount, LocalDateTime startedAt,
		LocalDateTime expirationAt, int usePeriod) {
		this.name = name;
		this.type = type;
		this.discountAmount = discountAmount;
		this.discountType = discountType;
		this.maxDiscountAmount = maxDiscountAmount;
		this.minPurchaseAmount = minPurchaseAmount;
		this.startedAt = startedAt;
		this.expirationAt = expirationAt;
		this.usePeriod = usePeriod;
	}

	/**
	 * 주어진 요청으로부터 {@code CouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code CouponTemplate} 객체
	 */
	public static CouponTemplate of(CreateCouponTemplateRequest request) {
		return CouponTemplate.builder()
			.name(request.name())
			.type(request.type())
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.usePeriod(request.usePeriod())
			.build();
	}

	/**
	 * 주어진 도서 쿠폰 템플릿 요청으로부터 {@code CouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 도서 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code CouponTemplate} 객체
	 */
	public static CouponTemplate of(CreateBookCouponTemplateRequest request) {
		return CouponTemplate.builder()
			.name(request.name())
			.type(CouponType.BOOK)
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.usePeriod(request.usePeriod())
			.build();
	}

	/**
	 * 주어진 카테고리 쿠폰 템플릿 요청으로부터 {@code CouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code CouponTemplate} 객체
	 */
	public static CouponTemplate of(CreateCategoryCouponTemplateRequest request) {
		return CouponTemplate.builder()
			.name(request.name())
			.type(CouponType.CATEGORY)
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.usePeriod(request.usePeriod())
			.build();
	}

	/**
	 * 주어진 카테고리 쿠폰 템플릿 요청으로부터 {@code CouponTemplate} 객체를 생성합니다.
	 *
	 * @param request 카테고리 쿠폰 템플릿 생성 요청
	 * @return 생성된 {@code CouponTemplate} 객체
	 */
	public static CouponTemplate of(CreateLimitedCouponTemplateRequest request) {
		return CouponTemplate.builder()
			.name(request.name())
			.type(CouponType.LIMITED)
			.discountAmount(request.discountAmount())
			.discountType(request.discountType())
			.maxDiscountAmount(request.maxDiscountAmount())
			.minPurchaseAmount(request.minPurchaseAmount())
			.startedAt(request.startedAt())
			.expirationAt(request.expirationAt())
			.usePeriod(request.usePeriod())
			.build();
	}
}
