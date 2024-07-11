package store.novabook.coupon.common.messaging.dto;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
	PaymentType type,
	Long memberId,
	@NotNull
	UUID orderId,
	Object paymentInfo
) implements Serializable {}
