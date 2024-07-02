package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.coupon.dto.request.CreateCouponTemplateRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponTemplateResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponTemplateResponse;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponTemplateService;

/**
 * {@code CouponTemplateServiceImpl} 클래스는 쿠폰 템플릿에 대한 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CouponTemplateServiceImpl implements CouponTemplateService {

	private final CouponTemplateRepository couponTemplateRepository;

	/**
	 * 쿠폰 타입을 검증합니다.
	 *
	 * @param type 쿠폰 타입
	 * @throws BadRequestException 유효하지 않은 쿠폰 타입인 경우
	 */
	private static void validateType(CouponType type) {
		if (type == CouponType.BOOK || type == CouponType.CATEGORY) {
			throw new BadRequestException(ErrorCode.INVALID_COUPON_TYPE);
		}
	}

	/**
	 * 주어진 타입과 유효성 여부에 따라 쿠폰 템플릿을 조회합니다.
	 *
	 * @param type     쿠폰 타입
	 * @param isValid  유효성 여부
	 * @param pageable 페이지 정보
	 * @return 주어진 조건에 맞는 쿠폰 템플릿의 페이지
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponTemplateResponse> findByTypeAndValid(CouponType type, boolean isValid, Pageable pageable) {
		if (isValid) {
			return couponTemplateRepository.findAllByTypeAndStartedAtBeforeAndExpirationAtAfter(type,
				LocalDateTime.now(), LocalDateTime.now(), pageable).map(GetCouponTemplateResponse::fromEntity);
		}
		return couponTemplateRepository.findAllByType(type, pageable).map(GetCouponTemplateResponse::fromEntity);
	}

	/**
	 * 모든 쿠폰 템플릿을 페이지 형식으로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 모든 쿠폰 템플릿의 페이지
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetCouponTemplateResponse> findAll(Pageable pageable) {
		return couponTemplateRepository.findAll(pageable).map(GetCouponTemplateResponse::fromEntity);
	}

	/**
	 * 새로운 쿠폰 템플릿을 생성합니다.
	 *
	 * @param request 쿠폰 템플릿 생성 요청
	 * @return 생성된 쿠폰 템플릿의 응답
	 */
	@Override
	public CreateCouponTemplateResponse create(CreateCouponTemplateRequest request) {
		validateType(request.type());
		CouponTemplate couponTemplate = CouponTemplate.of(request);
		CouponTemplate saved = couponTemplateRepository.save(couponTemplate);
		return CreateCouponTemplateResponse.fromEntity(saved);
	}
}
