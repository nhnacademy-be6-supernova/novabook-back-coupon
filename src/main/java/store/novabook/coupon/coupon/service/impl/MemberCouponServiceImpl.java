package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.*;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.*;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;
import store.novabook.coupon.coupon.repository.querydsl.BookCouponQueryRepository;
import store.novabook.coupon.coupon.repository.querydsl.CategoryCouponQueryRepository;
import store.novabook.coupon.coupon.service.MemberCouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponRepository memberCouponRepository;
	private final CouponRepository couponRepository;
	private final CategoryCouponQueryRepository categoryCouponQueryRepository;
	private final BookCouponQueryRepository bookCouponQueryRepository;

	@Override
	public CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request) {
		Coupon coupon = couponRepository.findById(request.couponCode())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		if (coupon.getExpirationAt().isBefore(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON_CODE);
		}

		MemberCoupon memberCoupon = MemberCoupon.of(memberId, coupon, MemberCouponStatus.UNUSED);
		MemberCoupon saved = memberCouponRepository.save(memberCoupon);
		return CreateMemberCouponResponse.fromEntity(saved);
	}

	@Override
	public GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly) {
		List<MemberCoupon> generalCoupons = memberCouponRepository.findAllByMemberIdAndCoupon_CodeStartsWithAndStatusMatchesAndCoupon_ExpirationAtBeforeAndCoupon_StartedAtAfter(memberId, CouponType.GENERAL.getPrefix(),MemberCouponStatus.UNUSED, LocalDateTime.now(), LocalDateTime.now());
		List<BookCoupon> bookCoupons = bookCouponQueryRepository.findBookCouponsByMemberId(memberId, validOnly);
		List<CategoryCoupon> categoryCoupons = categoryCouponQueryRepository.findCategoryCouponsByMemberId(memberId, validOnly);

		List<GetCouponResponse> generalCouponResponses = generalCoupons.stream()
				.map(mc -> GetCouponResponse.fromEntity(mc.getCoupon()))
				.collect(Collectors.toList());

		List<GetCouponBookResponse> bookCouponResponses = bookCoupons.stream()
				.map(GetCouponBookResponse::fromEntity)
				.collect(Collectors.toList());

		List<GetCouponCategoryResponse> categoryCouponResponses = categoryCoupons.stream()
				.map(GetCouponCategoryResponse::fromEntity)
				.collect(Collectors.toList());

		return new GetMemberCouponByTypeResponse(generalCouponResponses, bookCouponResponses, categoryCouponResponses);
	}

	@Override
	public GetMemberCouponResponse getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status) {
		return null;
	}

}
