package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.coupon.common.adapter.StoreApi;
import store.novabook.coupon.common.adapter.dto.GetMemberIdAllResponse;
import store.novabook.coupon.common.adapter.dto.GetMemberIdWithBirthdayRequest;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.message.MemberRegistrationMessage;
import store.novabook.coupon.coupon.domain.Coupon;
import store.novabook.coupon.coupon.domain.CouponType;
import store.novabook.coupon.coupon.domain.MemberCoupon;
import store.novabook.coupon.coupon.domain.MemberCouponStatus;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponResponse;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;
import store.novabook.coupon.coupon.service.MemberCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponRepository memberCouponRepository;
	private final CouponRepository couponRepository;

	private final StoreApi storeApi;

	@Override
	@Transactional
	public CreateMemberCouponResponse saveMemberCoupon(Long memberId, CreateMemberCouponRequest request) {
		Coupon coupon = couponRepository.findById(request.couponCode())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		// 다운로드 불가능 기간
		if (coupon.getExpirationAt().isBefore(LocalDateTime.now()) || coupon.getStartedAt()
			.isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON_CODE);
		}

		MemberCoupon memberCoupon = MemberCoupon.of(memberId, coupon, MemberCouponStatus.UNUSED,
			request.expirationAt());
		MemberCoupon saved = memberCouponRepository.save(memberCoupon);
		return CreateMemberCouponResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly) {
		List<GetMemberCouponResponse> generalCouponList = memberCouponRepository.findGeneralCouponsByMemberId(memberId,
			validOnly);
		List<GetMemberCouponBookResponse> bookCouponList = memberCouponRepository.findBookCouponsByMemberId(memberId,
			validOnly);
		List<GetMemberCouponCategoryResponse> categoryCouponList = memberCouponRepository.findCategoryCouponsByMemberId(
			memberId, validOnly);

		return GetMemberCouponByTypeResponse.builder()
			.generalCouponList(generalCouponList)
			.bookCouponList(bookCouponList)
			.categoryCouponList(categoryCouponList)
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetMemberCouponResponse> getMemberCouponAllByStatus(Long memberId, MemberCouponStatus status,
		Pageable pageable) {
		Page<MemberCoupon> memberCouponPage = memberCouponRepository.findAllByStatus(status, pageable);
		return memberCouponPage.map(GetMemberCouponResponse::fromEntity);
	}

	@Override
	@Transactional
	public void updateMemberCouponStatus(Long memberId, String memberCouponId, PutMemberCouponRequest request) {
		MemberCoupon memberCoupon = memberCouponRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		validateMemberCoupon(memberId, memberCoupon);

		memberCoupon.updateStatus(request.status());
	}

	@Override
	@Transactional
	public CreateMemberCouponAllResponse saveMemberCouponAll(CreateMemberCouponAllRequest request) {
		Coupon coupon = couponRepository.findById(request.couponCode())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		List<MemberCoupon> memberCouponList = new ArrayList<>();
		for (Long id : request.memberIdList()) {
			MemberCoupon memberCoupon = MemberCoupon.builder()
				.memberId(id)
				.coupon(coupon)
				.expirationAt(request.expirationAt())
				.status(MemberCouponStatus.UNUSED)
				.build();
			memberCouponList.add(memberCoupon);
		}

		List<MemberCoupon> saved = memberCouponRepository.saveAll(memberCouponList);
		return CreateMemberCouponAllResponse.fromEntity(saved);
	}

	private void validateMemberCoupon(Long memberId, MemberCoupon memberCoupon) {
		if (!Objects.equals(memberCoupon.getMemberId(), memberId)) {
			throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
		}

		if (memberCoupon.getStatus() != MemberCouponStatus.UNUSED || memberCoupon.getExpirationAt()
			.isBefore(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON);
		}
	}

	@RabbitListener(queues = "${rabbitmq.queue.coupon}")
	@Transactional
	public void handleMemberRegistrationMessage(MemberRegistrationMessage message) {
		Coupon welcomeCoupon = couponRepository.findTopByCodeStartsWithOrderByCreatedAtDesc(
				CouponType.WELCOME.getPrefix())
			.orElseThrow(() -> new BadRequestException(ErrorCode.WELCOME_COUPON_NOT_FOUND));

		memberCouponRepository.save(MemberCoupon.of(message.memberId(), welcomeCoupon, MemberCouponStatus.UNUSED,
			LocalDateTime.now().plusHours(welcomeCoupon.getUsePeriod())));
	}

	@Scheduled(cron = "0 * * * * ?")
	@Transactional
	public void issueBirthdayCoupons() {
		Coupon birthdayCoupon = couponRepository.findTopByCodeStartsWithOrderByCreatedAtDesc(
				CouponType.BIRTHDAY.getPrefix())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BIRTHDAY_COUPON_NOT_FOUND));

		GetMemberIdWithBirthdayRequest request = GetMemberIdWithBirthdayRequest.builder()
			.month(LocalDate.now().getMonthValue())
			.build();
		GetMemberIdAllResponse memberIdList = storeApi.getMemberAllWithBirthdays(request);

		LocalDateTime nextStart = LocalDate.now().atStartOfDay().plusHours(birthdayCoupon.getUsePeriod());

		List<MemberCoupon> memberCoupons = new ArrayList<>();
		for (Long userId : memberIdList.memberIds()) {
			MemberCoupon memberCoupon = MemberCoupon.builder()
				.memberId(userId)
				.coupon(birthdayCoupon)
				.status(MemberCouponStatus.UNUSED)
				.expirationAt(nextStart)
				.build();
			memberCoupons.add(memberCoupon);
		}
		memberCouponRepository.saveAll(memberCoupons);
	}

}
