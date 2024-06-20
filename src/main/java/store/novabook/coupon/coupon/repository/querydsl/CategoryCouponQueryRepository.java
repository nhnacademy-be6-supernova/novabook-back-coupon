package store.novabook.coupon.coupon.repository.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.domain.QCategoryCoupon;
import store.novabook.coupon.coupon.domain.QCoupon;
import store.novabook.coupon.coupon.domain.QMemberCoupon;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;

@Repository
@Transactional(readOnly = true)
public class CategoryCouponQueryRepository extends QuerydslRepositorySupport {

	private final JPAQueryFactory queryFactory;

	public CategoryCouponQueryRepository(Class<?> domainClass, JPAQueryFactory query) {
		super(CategoryCoupon.class);
		this.queryFactory = query;
	}

	public List<MemberCategoryCouponDto> findCategoryCouponsByMemberId(Long memberId, boolean validOnly) {
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;
		QCoupon coupon = QCoupon.coupon;
		QCategoryCoupon categoryCoupon = QCategoryCoupon.categoryCoupon;

		var query = queryFactory.select(
				Projections.constructor(MemberCategoryCouponDto.class, memberCoupon.id, categoryCoupon))
			.from(categoryCoupon)
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
}
