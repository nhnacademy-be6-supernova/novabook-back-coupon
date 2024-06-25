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
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.adapter.dto.GetMemberIdAllResponse;
import store.novabook.coupon.common.adapter.dto.GetMemberIdWithBirthdayRequest;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.ForbiddenException;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.message.MemberRegistrationMessage;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.CreateMemberCouponAllRequest;
import store.novabook.coupon.coupon.dto.request.PutMemberCouponRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.CreateMemberCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponBookResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponByTypeResponse;
import store.novabook.coupon.coupon.dto.response.GetMemberCouponCategoryResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.repository.MemberCouponRepository;
import store.novabook.coupon.coupon.service.MemberCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponRepository memberCouponRepository;
	private final CouponRepository couponRepository;

	private final StoreAdapter storeAdapter;

	@Override
	@Transactional
	public CreateCouponResponse saveMemberCoupon(Long memberId, CreateCouponRequest request) {
		CouponTemplate couponTemplate = couponRepository.findById(request.couponCode())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		// 다운로드 불가능 기간
		if (couponTemplate.getExpirationAt().isBefore(LocalDateTime.now()) || couponTemplate.getStartedAt()
			.isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON_CODE);
		}

		Coupon coupon = Coupon.of(couponTemplate, CouponStatus.UNUSED, request.expirationAt());
		Coupon saved = memberCouponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public GetMemberCouponByTypeResponse getMemberCouponAllByValid(Long memberId, Boolean validOnly) {
		List<GetCouponResponse> generalCouponList = memberCouponRepository.findGeneralCouponsByMemberId(memberId,
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
	public Page<GetCouponResponse> getMemberCouponAllByStatus(Long memberId, CouponStatus status,
		Pageable pageable) {
		Page<Coupon> memberCouponPage = memberCouponRepository.findAllByStatus(status, pageable);
		return memberCouponPage.map(GetCouponResponse::fromEntity);
	}

	@Override
	@Transactional
	public void updateMemberCouponStatus(Long memberId, String memberCouponId, PutMemberCouponRequest request) {
		Coupon coupon = memberCouponRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		validateMemberCoupon(memberId, coupon);

		coupon.updateStatus(request.status());
	}

	@Override
	@Transactional
	public CreateMemberCouponAllResponse saveMemberCouponAll(CreateMemberCouponAllRequest request) {
		CouponTemplate couponTemplate = couponRepository.findById(request.couponCode())
			.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));

		List<Coupon> couponList = new ArrayList<>();
		for (Long id : request.memberIdList()) {
			Coupon coupon = Coupon.builder()
				.memberId(id)
				.coupon(couponTemplate)
				.expirationAt(request.expirationAt())
				.status(CouponStatus.UNUSED)
				.build();
			couponList.add(coupon);
		}

		List<Coupon> saved = memberCouponRepository.saveAll(couponList);
		return CreateMemberCouponAllResponse.fromEntity(saved);
	}

	private void validateMemberCoupon(Long memberId, Coupon coupon) {
		if (!Objects.equals(coupon.getMemberId(), memberId)) {
			throw new ForbiddenException(ErrorCode.NOT_ENOUGH_PERMISSION);
		}

		if (coupon.getStatus() != CouponStatus.UNUSED || coupon.getExpirationAt().isBefore(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON);
		}
	}

	@RabbitListener(queues = "${rabbitmq.queue.coupon}")
	@Transactional
	public void handleMemberRegistrationMessage(MemberRegistrationMessage message) {
		CouponTemplate welcomeCouponTemplate = couponRepository.findTopByCodeStartsWithOrderByCreatedAtDesc(
				CouponType.WELCOME.getPrefix())
			.orElseThrow(() -> new BadRequestException(ErrorCode.WELCOME_COUPON_NOT_FOUND));

		memberCouponRepository.save(Coupon.of(message.memberId(), welcomeCouponTemplate, CouponStatus.UNUSED,
			LocalDateTime.now().plusHours(welcomeCouponTemplate.getUsePeriod())));
	}

	@Scheduled(cron = "0 0 0 1 * ?")
	@Transactional
	public void issueBirthdayCoupons() {
		CouponTemplate birthdayCouponTemplate = couponRepository.findTopByCodeStartsWithOrderByCreatedAtDesc(
				CouponType.BIRTHDAY.getPrefix())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BIRTHDAY_COUPON_NOT_FOUND));

		GetMemberIdWithBirthdayRequest request = GetMemberIdWithBirthdayRequest.builder()
			.month(LocalDate.now().getMonthValue())
			.build();
		GetMemberIdAllResponse memberIdList = storeAdapter.getMemberAllWithBirthdays(request);

		LocalDateTime nextStart = LocalDate.now().atStartOfDay().plusHours(birthdayCouponTemplate.getUsePeriod());

		List<Coupon> coupons = new ArrayList<>();
		for (Long userId : memberIdList.memberIds()) {
			Coupon coupon = Coupon.builder()
				.memberId(userId)
				.coupon(birthdayCouponTemplate)
				.status(CouponStatus.UNUSED)
				.expirationAt(nextStart)
				.build();
			coupons.add(coupon);
		}
		memberCouponRepository.saveAll(coupons);
	}

}
