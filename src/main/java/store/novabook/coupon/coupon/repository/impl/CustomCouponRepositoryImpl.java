package store.novabook.coupon.coupon.repository.impl;

import static store.novabook.coupon.coupon.entity.QBookCouponTemplate.*;
import static store.novabook.coupon.coupon.entity.QCategoryCouponTemplate.*;
import static store.novabook.coupon.coupon.entity.QCoupon.*;
import static store.novabook.coupon.coupon.entity.QCouponTemplate.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.repository.CustomCouponRepository;

/**
 * {@code CustomCouponRepositoryImpl} 클래스는 커스텀 쿠폰 리포지토리 구현체입니다.
 */
@Transactional(readOnly = true)
public class CustomCouponRepositoryImpl extends QuerydslRepositorySupport implements CustomCouponRepository {

	/**
	 * {@code CustomCouponRepositoryImpl} 생성자.
	 */
	public CustomCouponRepositoryImpl() {
		super(Coupon.class);
	}

	/**
	 * 주어진 요청에 따라 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param request 쿠폰 조회 요청
	 * @return 조회된 쿠폰 응답 리스트
	 */
	@Override
	public List<GetCouponResponse> findSufficientCoupons(GetCouponAllRequest request) {
		List<GetCouponResponse> category = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, coupon.status,
					couponTemplate.name, couponTemplate.discountAmount, couponTemplate.discountType,
					couponTemplate.maxDiscountAmount, couponTemplate.minPurchaseAmount, coupon.createdAt,
					coupon.expirationAt, coupon.usedAt))
			.innerJoin(couponTemplate)
			.on(coupon.couponTemplate.id.eq(couponTemplate.id))
			.innerJoin(categoryCouponTemplate)
			.on(categoryCouponTemplate.couponTemplate.id.eq(couponTemplate.id))
			.where(coupon.id.in(request.couponIdList())
				.and(categoryCouponTemplate.categoryId.in(request.categoryIdList()))
				.and(coupon.status.eq(CouponStatus.UNUSED))
				.and(coupon.expirationAt.after(LocalDateTime.now())))
			.fetch();
		List<GetCouponResponse> response = new ArrayList<>(category);

		List<GetCouponResponse> book = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, coupon.status,
					couponTemplate.name, couponTemplate.discountAmount, couponTemplate.discountType,
					couponTemplate.maxDiscountAmount, couponTemplate.minPurchaseAmount, coupon.createdAt,
					coupon.expirationAt, coupon.usedAt))
			.innerJoin(couponTemplate)
			.on(coupon.couponTemplate.id.eq(couponTemplate.id))
			.innerJoin(bookCouponTemplate)
			.on(bookCouponTemplate.couponTemplate.id.eq(coupon.couponTemplate.id))
			.where(coupon.id.in(request.couponIdList())
				.and(bookCouponTemplate.bookId.in(request.bookIdList()))
				.and(coupon.status.eq(CouponStatus.UNUSED))
				.and(coupon.expirationAt.after(LocalDateTime.now())))
			.fetch();
		response.addAll(book);

		List<GetCouponResponse> general = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, coupon.status,
					couponTemplate.name, couponTemplate.discountAmount, couponTemplate.discountType,
					couponTemplate.maxDiscountAmount, couponTemplate.minPurchaseAmount, coupon.createdAt,
					coupon.expirationAt, coupon.usedAt))
			.where(coupon.id.in(request.couponIdList())
				.and(coupon.couponTemplate.type.notIn(CouponType.BOOK, CouponType.CATEGORY))
				.and(coupon.status.eq(CouponStatus.UNUSED))
				.and(coupon.expirationAt.after(LocalDateTime.now())))
			.fetch();
		response.addAll(general);

		return response;
	}
}
