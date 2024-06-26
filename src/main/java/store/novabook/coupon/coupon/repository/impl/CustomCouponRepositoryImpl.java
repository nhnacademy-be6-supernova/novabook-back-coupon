package store.novabook.coupon.coupon.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
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

		BooleanBuilder builder = new BooleanBuilder();

		// Book 조건
		BooleanBuilder bookCondition = new BooleanBuilder();
		if (request.bookIdList() != null && !request.bookIdList().isEmpty()) {
			bookCondition.and(couponTemplate.type.eq(CouponType.BOOK)
				.and(bookCouponTemplate.bookId.in(request.bookIdList())));
		}

		// Category 조건
		BooleanBuilder categoryCondition = new BooleanBuilder();
		if (request.categoryIdList() != null && !request.categoryIdList().isEmpty()) {
			categoryCondition.and(couponTemplate.type.eq(CouponType.CATEGORY)
				.and(categoryCouponTemplate.categoryId.in(request.categoryIdList())));
		}

		// 다른 타입 조건
		BooleanBuilder otherCondition = new BooleanBuilder();
		otherCondition.and(couponTemplate.type.ne(CouponType.BOOK).and(couponTemplate.type.ne(CouponType.CATEGORY)));

		// 최종 조건
		builder.and(bookCondition.or(categoryCondition).or(otherCondition));

		return select(
			Projections.constructor(GetCouponResponse.class, coupon.id, couponTemplate.type, couponTemplate.name,
				couponTemplate.discountAmount, couponTemplate.discountType, couponTemplate.maxDiscountAmount,
				couponTemplate.minPurchaseAmount, coupon.createdAt, coupon.expirationAt)).from(coupon)
			.join(coupon.couponTemplate, couponTemplate)
			.leftJoin(bookCouponTemplate)
			.on(couponTemplate.id.eq(bookCouponTemplate.id))
			.leftJoin(categoryCouponTemplate)
			.on(couponTemplate.id.eq(categoryCouponTemplate.id))
			.where(builder)
			.fetch();
	}
}
