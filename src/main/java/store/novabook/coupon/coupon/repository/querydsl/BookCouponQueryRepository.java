package store.novabook.coupon.coupon.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import store.novabook.coupon.coupon.domain.*;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookCouponQueryRepository extends QuerydslRepositorySupport {

	private final JPAQueryFactory queryFactory;

	public BookCouponQueryRepository(Class<?> domainClass, JPAQueryFactory query) {
		super(BookCoupon.class);
		this.queryFactory = query;
	}

	public List<BookCoupon> findBookCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QBookCoupon bookCoupon = QBookCoupon.bookCoupon;

		var query = queryFactory.selectFrom(bookCoupon)
				.join(bookCoupon.coupon, coupon)
				.join(memberCoupon).on(memberCoupon.coupon.code.eq(coupon.code))
				.where(memberCoupon.memberId.eq(memberId)
						.and(coupon.code.startsWith("B")));

		if (validOnly) {
			query.where(
					coupon.startedAt.before(LocalDateTime.now()),
					coupon.expirationAt.after(LocalDateTime.now())
			).where(memberCoupon.status.eq(MemberCouponStatus.UNUSED));
		}

		return query.fetch();
	}
}
