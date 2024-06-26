package store.novabook.coupon.coupon.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.coupon.common.exception.BadRequestException;
import store.novabook.coupon.common.exception.ErrorCode;
import store.novabook.coupon.coupon.dto.message.CouponCreatedMessage;
import store.novabook.coupon.coupon.dto.message.MemberRegistrationMessage;
import store.novabook.coupon.coupon.dto.request.CreateCouponRequest;
import store.novabook.coupon.coupon.dto.request.GetCouponAllRequest;
import store.novabook.coupon.coupon.dto.response.CreateCouponResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponAllResponse;
import store.novabook.coupon.coupon.dto.response.GetCouponResponse;
import store.novabook.coupon.coupon.entity.Coupon;
import store.novabook.coupon.coupon.entity.CouponStatus;
import store.novabook.coupon.coupon.entity.CouponTemplate;
import store.novabook.coupon.coupon.entity.CouponType;
import store.novabook.coupon.coupon.repository.CouponRepository;
import store.novabook.coupon.coupon.repository.CouponTemplateRepository;
import store.novabook.coupon.coupon.service.CouponService;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {
	private final CouponRepository couponRepository;
	private final CouponTemplateRepository couponTemplateRepository;
	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.coupon}")
	private String couponExchange;

	@Value("${rabbitmq.routing.couponCreated}")
	private String couponCreatedRoutingKey;

	private static void validateExpiration(CouponTemplate couponTemplate) {
		if (couponTemplate.getExpirationAt().isBefore(LocalDateTime.now()) || couponTemplate.getStartedAt()
			.isAfter(LocalDateTime.now())) {
			throw new BadRequestException(ErrorCode.EXPIRED_COUPON_CODE);
		}
	}

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
	public CreateCouponResponse create(CreateCouponRequest request) {
		CouponTemplate couponTemplate = couponTemplateRepository.findById(request.couponTemplateId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.COUPON_TEMPLATE_NOT_FOUND));

		validateExpiration(couponTemplate);

		Coupon coupon = Coupon.of(couponTemplate, CouponStatus.UNUSED,
			couponTemplate.getStartedAt().plusHours(couponTemplate.getUsePeriod()));
		Coupon saved = couponRepository.save(coupon);
		return CreateCouponResponse.fromEntity(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public GetCouponAllResponse findSufficientCouponAllById(GetCouponAllRequest request) {
		List<GetCouponResponse> sufficientCoupons = couponRepository.findSufficientCoupons(request);
		return GetCouponAllResponse.builder().couponResponseList(sufficientCoupons).build();
	}

	@RabbitListener(queues = "${rabbitmq.queue.coupon}")
	@Transactional
	public void handleMemberRegistrationMessage(MemberRegistrationMessage message) {
		CouponTemplate welcomeCouponTemplate = couponTemplateRepository.findTopByTypeOrderByCreatedAtDesc(
			CouponType.WELCOME).orElseThrow(() -> new BadRequestException(ErrorCode.WELCOME_COUPON_NOT_FOUND));
		Coupon saved = couponRepository.save(Coupon.of(welcomeCouponTemplate, CouponStatus.UNUSED,
			LocalDateTime.now().plusHours(welcomeCouponTemplate.getUsePeriod())));
		CouponCreatedMessage couponCreatedMessage = new CouponCreatedMessage(saved.getId(), message.memberId());
		rabbitTemplate.convertAndSend(couponExchange, couponCreatedRoutingKey, couponCreatedMessage);

	}
}
