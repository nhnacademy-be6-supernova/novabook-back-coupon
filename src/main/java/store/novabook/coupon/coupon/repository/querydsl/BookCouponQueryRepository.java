package store.novabook.coupon.coupon.repository.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.domain.QBookCoupon;
import store.novabook.coupon.coupon.domain.QCoupon;
import store.novabook.coupon.coupon.domain.QMemberCoupon;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;

@Repository
@Transactional(readOnly = true)
public class BookCouponQueryRepository extends QuerydslRepositorySupport {

	private final JPAQueryFactory queryFactory;

	public BookCouponQueryRepository(Class<?> domainClass, JPAQueryFactory query) {
		super(BookCoupon.class);
		this.queryFactory = query;
	}

	public List<MemberBookCouponDto> findBookCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QBookCoupon bookCoupon = QBookCoupon.bookCoupon;

		var query = queryFactory.select(Projections.constructor(MemberBookCouponDto.class, memberCoupon.id, bookCoupon))
			.from(bookCoupon)
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
}
