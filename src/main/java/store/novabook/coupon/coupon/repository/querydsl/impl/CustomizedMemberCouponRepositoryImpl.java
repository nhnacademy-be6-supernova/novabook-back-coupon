package store.novabook.coupon.coupon.repository.querydsl.impl;

import static com.querydsl.jpa.JPAExpressions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.domain.QBookCoupon;
import store.novabook.coupon.coupon.domain.QCategoryCoupon;
import store.novabook.coupon.coupon.domain.QCoupon;
import store.novabook.coupon.coupon.domain.QMemberCoupon;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.repository.querydsl.CustomizedMemberCouponRepository;

public class CustomizedMemberCouponRepositoryImpl extends QuerydslRepositorySupport
	implements CustomizedMemberCouponRepository {

	public CustomizedMemberCouponRepositoryImpl() {
		super(MemberCoupon.class);
	}

	@Override
	public List<MemberCategoryCouponDto> findCategoryCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;

		JPQLQuery<MemberCategoryCouponDto> query = select(
			Projections.constructor(MemberCategoryCouponDto.class, memberCoupon.id, categoryCoupon)).from(
				categoryCoupon)
			.join(categoryCoupon.coupon, coupon)
			.join(memberCoupon)
			.on(memberCoupon.coupon.code.eq(coupon.code))
			.where(memberCoupon.memberId.eq(memberId).and(coupon.code.startsWith("C")));

		if (validOnly) {
			query.where(coupon.startedAt.before(LocalDateTime.now()), coupon.expirationAt.after(LocalDateTime.now()),
				memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return query.fetch();
	}

	public List<MemberBookCouponDto> findBookCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QBookCoupon bookCoupon = QBookCoupon.bookCoupon;

		JPQLQuery<MemberBookCouponDto> query = select(
			Projections.constructor(MemberBookCouponDto.class, memberCoupon.id, bookCoupon)).from(bookCoupon)
			.join(bookCoupon.coupon, coupon)
			.join(memberCoupon)
			.on(memberCoupon.coupon.code.eq(coupon.code))
			.where(memberCoupon.memberId.eq(memberId).and(coupon.code.startsWith("B")));

		if (validOnly) {
			query.where(coupon.startedAt.before(LocalDateTime.now()), coupon.expirationAt.after(LocalDateTime.now()),
				memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return query.fetch();
	}

	@Override
	public List<GetMemberCouponResponse> findValidCouponsByStatus(Long memberId, String prefix,
		MemberCouponStatus status, LocalDateTime expirationAt, LocalDateTime startedAt) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;

		return select(Projections.constructor(GetMemberCouponResponse.class,
			memberCoupon.id.as("memberCouponId"),
			coupon.code,
			coupon.name,
			coupon.discountAmount,
			coupon.discountType,
			coupon.maxDiscountAmount,
			coupon.minPurchaseAmount,
			coupon.startedAt,
			coupon.expirationAt))
			.from(memberCoupon)
			.join(memberCoupon.coupon, coupon)
			.where(
				memberCoupon.memberId.eq(memberId),
				coupon.code.startsWith(prefix),
				memberCoupon.status.eq(status),
				coupon.expirationAt.lt(expirationAt),
				coupon.startedAt.gt(startedAt)
			)
			.fetch();
	}

}
