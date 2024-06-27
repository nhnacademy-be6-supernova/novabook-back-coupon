package store.novabook.coupon.coupon.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.QBookCouponTemplate;
import store.novabook.coupon.coupon.entity.QCategoryCouponTemplate;
import store.novabook.coupon.coupon.entity.QCoupon;
import store.novabook.coupon.coupon.entity.QCouponTemplate;
import store.novabook.coupon.coupon.repository.CustomCouponRepository;

@Transactional(readOnly = true)
public class CustomCouponRepositoryImpl extends QuerydslRepositorySupport implements CustomCouponRepository {
	public CustomCouponRepositoryImpl() {
		super(Coupon.class);
	}

	@Override
	public List<GetCouponResponse> findSufficientCoupons(GetCouponAllRequest request) {
		QCoupon coupon = QCoupon.coupon;
		QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
		QBookCouponTemplate bookCouponTemplate = QBookCouponTemplate.bookCouponTemplate;
		QCategoryCouponTemplate categoryCouponTemplate = QCategoryCouponTemplate.categoryCouponTemplate;

		List<GetCouponResponse> category = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, couponTemplate.name,
					couponTemplate.discountAmount, couponTemplate.discountType, couponTemplate.maxDiscountAmount,
					couponTemplate.minPurchaseAmount, coupon.createdAt, coupon.expirationAt))
			.innerJoin(couponTemplate)
			.on(coupon.couponTemplate.id.eq(couponTemplate.id))
			.innerJoin(categoryCouponTemplate)
			.on(categoryCouponTemplate.couponTemplate.id.eq(couponTemplate.id))
			.where(coupon.id.in(request.couponIdList())
				.and(categoryCouponTemplate.categoryId.in(request.categoryIdList())))
			.fetch();
		List<GetCouponResponse> response = new ArrayList<>(category);

		List<GetCouponResponse> book = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, couponTemplate.name,
					couponTemplate.discountAmount, couponTemplate.discountType, couponTemplate.maxDiscountAmount,
					couponTemplate.minPurchaseAmount, coupon.createdAt, coupon.expirationAt))
			.innerJoin(couponTemplate)
			.on(coupon.couponTemplate.id.eq(couponTemplate.id))
			.innerJoin(bookCouponTemplate)
			.on(bookCouponTemplate.couponTemplate.id.eq(coupon.couponTemplate.id))
			.where(coupon.id.in(request.couponIdList()).and(bookCouponTemplate.bookId.in(request.bookIdList())))
			.fetch();
		response.addAll(book);

		List<GetCouponResponse> general = from(coupon).select(
				Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, couponTemplate.name,
					couponTemplate.discountAmount, couponTemplate.discountType, couponTemplate.maxDiscountAmount,
					couponTemplate.minPurchaseAmount, coupon.createdAt, coupon.expirationAt))
			.where(coupon.id.in(request.couponIdList())
				.and(coupon.couponTemplate.type.notIn(CouponType.BOOK, CouponType.CATEGORY)))
			.fetch();
		response.addAll(general);

		return response;
	}
}
