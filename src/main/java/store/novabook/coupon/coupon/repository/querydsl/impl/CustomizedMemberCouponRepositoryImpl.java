package store.novabook.coupon.coupon.repository.querydsl.impl;

import static com.querydsl.jpa.JPAExpressions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;

import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.domain.QBookCoupon;
import store.novabook.coupon.coupon.domain.QCategoryCoupon;
import store.novabook.coupon.coupon.domain.QCoupon;
import store.novabook.coupon.coupon.domain.QMemberCoupon;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.repository.querydsl.CustomizedMemberCouponRepository;

@Transactional(readOnly = true)
public class CustomizedMemberCouponRepositoryImpl extends QuerydslRepositorySupport
	implements CustomizedMemberCouponRepository {

	public CustomizedMemberCouponRepositoryImpl() {
		super(MemberCoupon.class);
	}

	@Override
	public List<GetMemberCouponCategoryResponse> findCategoryCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(memberCoupon.memberId.eq(memberId));

		if (validOnly) {
			builder.and(memberCoupon.expirationAt.after(LocalDateTime.now()))
				.and(memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return from(memberCoupon).select(
				Projections.constructor(GetMemberCouponCategoryResponse.class, memberCoupon.id, categoryCoupon.categoryId,
					coupon.code, coupon.name, coupon.discountAmount, coupon.discountType, coupon.maxDiscountAmount,
					coupon.minPurchaseAmount, memberCoupon.createdAt, memberCoupon.expirationAt))
			.innerJoin(categoryCoupon)
			.on(memberCoupon.coupon.code.eq(categoryCoupon.couponCode))
			.innerJoin(coupon)
			.on(categoryCoupon.couponCode.eq(coupon.code))
			.where(builder)
			.fetch();
	}

	@Override
	public List<GetMemberCouponBookResponse> findBookCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QBookCoupon bookCoupon = QBookCoupon.bookCoupon;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(memberCoupon.memberId.eq(memberId));

		if (validOnly) {
			builder.and(memberCoupon.expirationAt.after(LocalDateTime.now()))
				.and(memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return from(memberCoupon).select(
				Projections.constructor(GetMemberCouponBookResponse.class, memberCoupon.id, bookCoupon.bookId, coupon.code,
					coupon.name, coupon.discountAmount, coupon.discountType, coupon.maxDiscountAmount,
					coupon.minPurchaseAmount, memberCoupon.createdAt, memberCoupon.expirationAt))
			.innerJoin(bookCoupon)
			.on(memberCoupon.coupon.code.eq(bookCoupon.couponCode))
			.innerJoin(coupon)
			.on(bookCoupon.couponCode.eq(coupon.code))
			.where(builder)
			.fetch();
	}

	@Override
	public List<GetMemberCouponResponse> findGeneralCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(memberCoupon.memberId.eq(memberId)).and(coupon.code.notLike("C%")).and(coupon.code.notLike("B%"));

		if (validOnly) {
			builder.and(memberCoupon.expirationAt.after(LocalDateTime.now()))
				.and(memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return from(memberCoupon).select(
				Projections.constructor(GetMemberCouponResponse.class, memberCoupon.id, coupon.code, coupon.name,
					coupon.discountAmount, coupon.discountType, coupon.maxDiscountAmount, coupon.minPurchaseAmount,
					coupon.startedAt, coupon.expirationAt))
			.innerJoin(coupon)
			.on(memberCoupon.coupon.code.eq(coupon.code))
			.where(builder)
			.fetch();
	}

	@Override
	public List<GetMemberCouponResponse> findValidCouponsByStatus(Long memberId, String prefix,
		MemberCouponStatus status, LocalDateTime expirationAt, LocalDateTime startedAt) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;

		return select(
			Projections.constructor(GetMemberCouponResponse.class, memberCoupon.id.as("memberCouponId"), coupon.code,
				coupon.name, coupon.discountAmount, coupon.discountType, coupon.maxDiscountAmount,
				coupon.minPurchaseAmount, coupon.startedAt, coupon.expirationAt)).from(memberCoupon)
			.join(memberCoupon.coupon, coupon)
			.where(memberCoupon.memberId.eq(memberId), coupon.code.startsWith(prefix), memberCoupon.status.eq(status),
				coupon.expirationAt.lt(expirationAt), coupon.startedAt.gt(startedAt))
			.fetch();
	}

}
