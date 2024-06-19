package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.BookCoupon;
import store.novabook.coupon.coupon.domain.CategoryCoupon;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
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
	@Transactional(readOnly = true)
	public GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly) {
		List<MemberCoupon> generalCouponList = memberCouponRepository.findAllByMemberIdAndCoupon_CodeStartsWithAndStatusMatchesAndCoupon_ExpirationAtBeforeAndCoupon_StartedAtAfter(
			memberId, CouponType.GENERAL.getPrefix(), MemberCouponStatus.UNUSED, LocalDateTime.now(),
			LocalDateTime.now());
		List<BookCoupon> bookCouponList = bookCouponQueryRepository.findBookCouponsByMemberId(memberId, validOnly);
		List<CategoryCoupon> categoryCouponList = categoryCouponQueryRepository.findCategoryCouponsByMemberId(memberId,
			validOnly);

		List<GetCouponResponse> generalCouponResponseList = generalCouponList.stream()
			.map(mc -> GetCouponResponse.fromEntity(mc.getCoupon()))
			.collect(Collectors.toList());

		List<GetCouponBookResponse> bookCouponResponseList = bookCouponList.stream()
			.map(GetCouponBookResponse::fromEntity)
			.collect(Collectors.toList());

		List<GetCouponCategoryResponse> categoryCouponResponseList = categoryCouponList.stream()
			.map(GetCouponCategoryResponse::fromEntity)
			.collect(Collectors.toList());

		return GetMemberCouponByTypeResponse.builder()
			.generalCouponList(generalCouponResponseList)
			.bookCouponList(bookCouponResponseList)
			.categoryCouponList(categoryCouponResponseList)
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public GetMemberCouponResponse getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status,
		Pageable pageable) {
		Page<MemberCoupon> memberCouponPage = memberCouponRepository.findAllByStatus(status, pageable);
		return GetMemberCouponResponse.fromEntity(memberId, memberCouponPage);
	}

}
