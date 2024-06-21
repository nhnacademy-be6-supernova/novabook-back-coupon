package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.MemberBookCouponDto;
import store.novabook.coupon.coupon.dto.MemberCategoryCouponDto;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.repository.BookCouponRepository;
import store.novabook.coupon.coupon.repository.CategoryCouponRepository;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;
import store.novabook.coupon.coupon.service.MemberCouponService;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponRepository memberCouponRepository;
	private final CouponRepository couponRepository;
	private final CategoryCouponRepository categoryCouponRepository;
	private final BookCouponRepository bookCouponRepository;

	@Override
	@Transactional
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
		List<GetMemberCouponResponse> generalCouponList = memberCouponRepository.findGeneralCouponsByMemberId(memberId,
			validOnly);
		List<MemberBookCouponDto> bookCouponList = memberCouponRepository.findBookCouponsByMemberId(memberId,
			validOnly);
		List<MemberCategoryCouponDto> categoryCouponList = memberCouponRepository.findCategoryCouponsByMemberId(
			memberId, validOnly);

		return GetMemberCouponByTypeResponse.builder()
			.generalCouponList(generalCouponList)
			.bookCouponList(bookCouponList)
			.categoryCouponList(categoryCouponList)
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public GetMemberCouponAllResponse getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status,
		Pageable pageable) {
		Page<MemberCoupon> memberCouponPage = memberCouponRepository.findAllByStatus(status, pageable);
		return GetMemberCouponAllResponse.fromEntity(memberId, memberCouponPage);
	}

	@Override
	@Transactional
	public void updateMemberCouponStatus(Long memberId, PutMemberCouponRequest request) {
		MemberCoupon memberCoupon = memberCouponRepository.findById(request.memberCouponId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		validateMemberCoupon(memberId, memberCoupon);

		memberCoupon.updateStatus(request.status());
	}

	private void validateMemberCoupon(Long memberId, MemberCoupon memberCoupon) {
		if (!Objects.equals(memberCoupon.getMemberId(), memberId)) {
			throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
		}

		if (memberCoupon.getStatus() != MemberCouponStatus.UNUSED || memberCoupon.getCoupon()
			.getExpirationAt()
			.isBefore(LocalDateTime.now()) || memberCoupon.getCoupon().getStartedAt().isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON);
		}
	}

}
