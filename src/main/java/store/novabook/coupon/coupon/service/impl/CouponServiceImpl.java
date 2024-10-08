package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.adapter.StoreAdapter;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ConflictException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.common.exception.NotFoundException;
import store.novabook.coupon.common.messaging.dto.CreateCouponMessage;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.entity.LimitedCouponTemplate;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.repository.LimitedCouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponService;

/**
 * {@code CouponServiceImpl} 클래스는 쿠폰 서비스의 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final CouponTemplateRepository couponTemplateRepository;
	private final LimitedCouponTemplateRepository limitedCouponTemplateRepository;

	private final StoreAdapter storeAdapter;

	/**
	 * 쿠폰의 상태를 사용으로 업데이트합니다.
	 *
	 * @param id 쿠폰 ID
	 * @throws BadRequestException 쿠폰이 존재하지 않거나 이미 사용된 경우
	 */
	@Override
	public void updateStatusToUsed(Long id) {
		Coupon coupon = couponRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_NOT_FOUND));
		if (coupon.getStatus() == CouponStatus.USED) {
			throw new BadRequestException(ErrorCode.ALREADY_USED_COUPON);
		}
		coupon.updateStatus(CouponStatus.USED);
	}

	@Override
	public void updateStatus(Long id, CouponStatus status) {
		Coupon coupon = couponRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_NOT_FOUND));
		coupon.updateStatus(CouponStatus.USED);
	}

	/**
	 * 새로운 쿠폰을 생성합니다.
	 *
	 * @param request 쿠폰 생성 요청
	 * @return 생성된 쿠폰 응답
	 */
	@Override
	public CreateCouponResponse create(CreateCouponRequest request) {
		CouponTemplate couponTemplate = couponTemplateRepository.findById(request.couponTemplateId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_TEMPLATE_NOT_FOUND));

		validateDuplicated(request.couponTemplateId(), request.couponIdList());
		validateExpiration(couponTemplate);
		checkQuantity(couponTemplate);

		Coupon coupon = Coupon.of(couponTemplate, CouponStatus.UNUSED,
			LocalDateTime.now().plusHours(couponTemplate.getUsePeriod()));
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	/**
	 * 요청된 조건에 따라 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param request 쿠폰 조회 요청
	 * @return 조회된 쿠폰 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCouponAllResponse findSufficientCouponAllById(GetCouponAllRequest request) {
		List<GetCouponResponse> sufficientCoupons = couponRepository.findSufficientCoupons(request);
		return GetCouponAllResponse.builder().couponResponseList(sufficientCoupons).build();
	}

	/**
	 * 주어진 쿠폰 ID 리스트와 상태에 따라 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @param status       쿠폰 상태
	 * @param pageable     페이징 정보
	 * @return 조회된 쿠폰 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<GetCouponResponse> findAllByIdAndStatus(List<Long> couponIdList, CouponStatus status,
		Pageable pageable) {
		Page<Coupon> couponList = couponRepository.findAllByIdInAndStatus(couponIdList, status, pageable);
		return couponList.map(GetCouponResponse::fromEntity);
	}

	/**
	 * 주어진 쿠폰 ID 리스트에 따라 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @param pageable     페이징 정보
	 * @return 조회된 쿠폰 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public Page<GetCouponResponse> findAllById(List<Long> couponIdList, Pageable pageable) {
		Page<Coupon> couponList = couponRepository.findAllByIdIn(couponIdList, pageable);
		return couponList.map(GetCouponResponse::fromEntity);
	}

	/**
	 * 주어진 쿠폰 ID 리스트로 유효한 모든 쿠폰을 조회합니다.
	 *
	 * @param couponIdList 쿠폰 ID 리스트
	 * @return 조회된 쿠폰 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCouponAllResponse findAllValidById(List<Long> couponIdList) {
		List<Coupon> couponList = couponRepository.findAllByIdInAndStatusAndExpirationAtAfter(couponIdList,
			CouponStatus.UNUSED, LocalDateTime.now());
		return GetCouponAllResponse.fromEntity(couponList);
	}

	// 선착순 쿠폰
	@Override
	public CreateCouponResponse createByMessage(CreateCouponMessage message) {
		CouponTemplate couponTemplate;
		if (message.couponType().equals(CouponType.WELCOME) || message.couponType().equals(CouponType.BIRTHDAY)) {
			couponTemplate = couponTemplateRepository.findTopByTypeOrderByCreatedAtDesc(message.couponType())
				.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
		} else {
			couponTemplate = couponTemplateRepository.findById(message.couponTemplateId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
		}

		validateDuplicated(message.couponTemplateId(), message.couponIdList());
		validateExpiration(couponTemplate);
		checkQuantity(couponTemplate);

		Coupon coupon = Coupon.of(couponTemplate, CouponStatus.UNUSED,
			couponTemplate.getStartedAt().plusHours(couponTemplate.getUsePeriod()));
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Override
	public Coupon findById(Long id) {
		Optional<Coupon> optionalCoupon = couponRepository.findById(id);
		if (optionalCoupon.isEmpty()) {
			throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
		}

		return optionalCoupon.get();
	}

	private void checkQuantity(CouponTemplate couponTemplate) {
		if (couponTemplate.getType().equals(CouponType.LIMITED)) {
			LimitedCouponTemplate limitedCouponTemplate = limitedCouponTemplateRepository.findById(
				couponTemplate.getId()).orElseThrow(() -> new ConflictException(ErrorCode.CONFLICT_COUPON_TYPE));
			validateLimitedCoupon(limitedCouponTemplate);
			limitedCouponTemplate.decrementCouponQuantity();
		}

	}

	/**
	 * 쿠폰 템플릿의 유효성을 검증합니다.
	 *
	 * @param couponTemplate 쿠폰 템플릿
	 * @throws BadRequestException 쿠폰 템플릿이 만료되었거나 아직 시작되지 않은 경우
	 */
	protected void validateExpiration(CouponTemplate couponTemplate) {
		if (couponTemplate.getExpirationAt().isBefore(LocalDateTime.now()) || couponTemplate.getStartedAt()
			.isAfter(LocalDateTime.now())) {
			if (couponTemplate.getType().equals(CouponType.WELCOME) || couponTemplate.getType()
				.equals(CouponType.BIRTHDAY)) {
				throw new NotFoundException(ErrorCode.EXPIRED_COUPON);
			}
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON);
		}
	}

	private void validateDuplicated(Long couponTemplateId, List<Long> couponIdList) {
		if (!couponIdList.isEmpty() && Boolean.TRUE.equals(
			couponRepository.existsByIdInAndCouponTemplateId(couponIdList, couponTemplateId))) {
			throw new BadRequestException(ErrorCode.COUPON_ALREADY_EXIST);
		}
	}

	private void validateLimitedCoupon(LimitedCouponTemplate limitedCouponTemplate) {
		if (limitedCouponTemplate.getQuantity() <= 0) {
			throw new ConflictException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
		}
	}

}
